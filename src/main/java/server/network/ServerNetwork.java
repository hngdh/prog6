package server.network;

import common.exceptions.LogException;
import common.exceptions.NetworkException;
import common.io.LogUtil;
import common.io.Printer;
import common.packets.Request;
import common.packets.Response;
import server.iostream.Handler;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;

public class ServerNetwork {
    private final int PORT = 4004;
    private final int MAX_PACKET_SIZE = 65536;
    private Handler handler;
    private Selector selector;

    public ServerNetwork() {
    }

    public void init(String fileName) throws NetworkException, LogException {
        LogUtil.logServerInfo("Server started");
        handler = new Handler();
        handler.prepare(fileName);
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.socket().bind(new InetSocketAddress("0.0.0.0", PORT));
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            handle();
        } catch (IOException e) {
            Printer.printError("Error opening channel");
            LogUtil.logServerError(e);
            throw new NetworkException();
        }
    }

    public void handle() throws LogException, IOException {
        LogUtil.logServerInfo("Listening on port " + PORT);
        while (true) {
            if (selector.select(10) == 0) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                if (br.ready()) {
                    String str = br.readLine();
                    handler.processServer(new Request(str, null, null));
                }
            }
            for (SelectionKey key : selector.selectedKeys()) {
                if (key.channel() instanceof DatagramChannel dc) {
                    Response response;
                    Request request = new Request("", "", null);
                    try {
                        request = readRequest(dc, MAX_PACKET_SIZE);
                        response = handler.processClient(request.getAddress(), request);
                        response.addNotice(request.getCommand() + " command executed");
                    } catch (LogException e) {
                        LogUtil.logServerError(e);
                        List<String> notice = new ArrayList<>();
                        notice.add("Server's error executing " + request.getCommand() + " command");
                        notice.add(request.getCommand() + " command not executed.");
                        response = new Response(notice, null);
                    }
                    sendResponse(response, request.getAddress(), dc);
                }
            }
            selector.selectedKeys().clear();
        }
    }

    public void emergencyShutdown() throws LogException {
        LogUtil.logServerInfo("A emergency shutdown process has been called!");
        LogUtil.logServerInfo("Shutting down, a backup process is being executed...");
        handler.processClient(null, new Request("save", null, null));
    }

    public Request readRequest(DatagramChannel ss, int MAX_PACKET_SIZE) throws LogException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
            SocketAddress remoteAddress = ss.receive(buffer);
            LogUtil.logServerInfo("Received connection from: " + remoteAddress);
            buffer.flip();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Request request = (Request) ois.readObject();
            buffer.clear();

            request.setAddress(remoteAddress);
            return request;
        } catch (IOException | ClassNotFoundException e) {
            Printer.printError("Error during channel reading");
            LogUtil.logServerError(e);
            throw new LogException();
        }
    }

    public void sendResponse(Response response, SocketAddress port, DatagramChannel channel) throws LogException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            oos.flush();
            ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
            channel.send(buffer, port);
            LogUtil.logServerInfo("Response sent to " + port);
        } catch (IOException e) {
            LogUtil.logServerError(e);
            Printer.printError("Error during sending process");
            throw new LogException();
        }
    }
}
