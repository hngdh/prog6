package client.network;

import common.exceptions.LogException;
import common.exceptions.NetworkException;
import common.exceptions.ServerOfflineException;
import common.io.LogUtil;
import common.io.Printer;
import common.packets.Request;
import common.packets.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class ClientNetwork {
    private static final Logger log = LoggerFactory.getLogger(ClientNetwork.class);
    private final int PORT = 4004;
    private final int MAX_PACKET_SIZE = 65536;
    private DatagramChannel channel;
    private SocketAddress serverAddress;

    public ClientNetwork() {
    }

    public void connect() throws NetworkException, LogException {
        try {
            channel = DatagramChannel.open();
            serverAddress = new InetSocketAddress("localhost", PORT);
            channel.connect(serverAddress);
        } catch (IOException e) {
            LogUtil.logClientError(e);
            throw new ServerOfflineException();
        }
    }

    public void shutDown() throws NetworkException, LogException {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException e) {
            LogUtil.logClientError(e);
            throw new NetworkException();
        }
    }

    public Response respond(Request request) throws NetworkException, LogException {
        connect();
        sendPacket(channel, serverAddress, request);
        Response response = getResponse(channel, MAX_PACKET_SIZE);
        shutDown();
        return response;
    }

    public void sendPacket(DatagramChannel channel, SocketAddress serverAddress, Request request) throws LogException {
        Printer.printCondition("> Executing " + request.getCommand());
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            oos.flush();
            ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
            channel.send(buffer, serverAddress);
            LogUtil.logClientInfo("Sending " + request.getCommand() + " from " + channel.getLocalAddress());
        } catch (IOException e) {
            LogUtil.logClientError(e);
        }
    }

    public Response getResponse(DatagramChannel channel, int MAX_PACKET_SIZE) throws NetworkException, LogException {
        Response response = new Response();
        try {
            ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
            channel.receive(buffer);
            buffer.flip();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
            ObjectInputStream ois = new ObjectInputStream(bais);
            response = (Response) ois.readObject();
            buffer.clear();
            LogUtil.logClientInfo("Responded from " + channel.getLocalAddress());
        } catch (PortUnreachableException e) {
            Printer.printError("Server's port unreachable!");
            LogUtil.logClientError(e);
            throw new NetworkException();
        } catch (IOException | ClassNotFoundException e) {
            LogUtil.logClientError(e);
        }
        return response;
    }
}
