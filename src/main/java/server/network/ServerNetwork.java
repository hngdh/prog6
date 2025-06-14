package server.network;

import common.exceptions.LogException;
import common.io.LogUtil;
import common.io.Printer;
import common.packets.Request;
import common.packets.Response;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;
import server.iostream.Handler;

public class ServerNetwork {
  private final int MAX_PACKET_SIZE = 65456;
  private int PORT = 4004;
  private Handler handler;
  private Selector selector;
  private boolean isAwaken;

  public ServerNetwork() {
    isAwaken = true;
  }

  public void init(String fileName) throws LogException {
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
      LogUtil.logServerErrorWONotif(e);
      portResolve(fileName);
    }
  }

  private void portResolve(String fileName) throws LogException {
    for (int i = 0; i < 45; i++) {
      PORT += 1001;
      if (PORT == 50050) PORT = 4004;
      LogUtil.logServerInfo("Switching to port " + PORT);
      init(fileName);
    }
  }

  public void handle() throws LogException, IOException {
    LogUtil.logServerInfo("Listening on port " + PORT);
    while (isAwaken) {
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

  public void shutdown() {
    LogUtil.logServerInfo("Exit detected, saving files...");
    handler.processClient(null, new Request("save", null, null));
    isAwaken = false;
  }

  public Request readRequest(DatagramChannel dc, int MAX_PACKET_SIZE) throws LogException {
    try {
      ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
      SocketAddress remoteAddress = dc.receive(buffer);
      buffer.flip();
      ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
      ObjectInputStream ois = new ObjectInputStream(bais);
      Request request = (Request) ois.readObject();
      LogUtil.logServerInfo("Received '" + request.getCommand() + "' from: " + remoteAddress);
      buffer.clear();
      request.setAddress(remoteAddress);
      return request;
    } catch (IOException | ClassNotFoundException e) {
      Printer.printError("Error during channel reading");
      LogUtil.logServerError(e);
      throw new LogException();
    }
  }

  public void sendResponse(Response response, SocketAddress port, DatagramChannel channel)
      throws LogException {
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
