package client.network;

import common.dataProcessors.InputReader;
import common.exceptions.NetworkException;
import common.io.LogUtil;
import common.io.Printer;
import common.packets.Request;
import common.packets.Response;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ClientNetwork {
  private final int MAX_PACKET_SIZE = 65536;
  private final ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
  private final InputReader reader = new InputReader();
  private int PORT = 404;
  private DatagramChannel channel;
  private SocketAddress serverAddress;

  public ClientNetwork() {
    reader.setReader();
    ping();
  }

  public void connect() {
    try {
      channel = DatagramChannel.open();
      serverAddress = new InetSocketAddress("127.0.0.1", PORT);
      channel.connect(serverAddress);
    } catch (IOException e) {
      LogUtil.logClientErrorWONotif(e);
      Printer.printError("Error opening channel");
    }
  }

  public void shutdown() {
    try {
      if (channel != null) channel.close();
    } catch (IOException e) {
      LogUtil.logClientErrorWONotif(e);
    }
  }

  public Response respond(Request request) {
    try {
      connect();
      sendPacket(channel, serverAddress, request);
      Response response = getResponse(channel);
      shutdown();
      return response;
    } catch (NetworkException e) {
      ping();
    }
    return null;
  }

  // Configurable port
  public void ping() {
    Request request = new Request("ping", null, null);
    try {
      connect();
      sendPacket(channel, serverAddress, request);
      getResponse(channel);
      Printer.printInfo("Connected on port " + PORT);
      shutdown();
    } catch (NetworkException e) {
      Printer.printCondition("Server not established on port " + PORT + ", please choose another");
      portResolve();
    }
  }

  public void portResolve() {
    try {
      int port = Integer.parseInt(reader.readLine());
      if (port > 65535 || port < 0) {
        Printer.printError("Port number can't exceed range [0-65535]");
        portResolve();
      } else {
        PORT = port;
        ping();
      }
    } catch (IOException | NumberFormatException f) {
      Printer.printError("Wrong input/ number format");
      portResolve();
    }
  }

  public void sendPacket(DatagramChannel channel, SocketAddress serverAddress, Request request) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(request);
      oos.flush();
      ByteBuffer buf = ByteBuffer.wrap(baos.toByteArray());
      channel.send(buf, serverAddress);
      LogUtil.logClientInfo(
          "Sending '" + request.getCommand() + "' from " + channel.getLocalAddress());
    } catch (IOException e) {
      LogUtil.logClientErrorWONotif(e);
    }
  }

  public Response getResponse(DatagramChannel channel) throws NetworkException {
    Response response = new Response();
    try {
      channel.receive(buffer);
      buffer.flip();
      ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
      ObjectInputStream ois = new ObjectInputStream(bais);
      response = (Response) ois.readObject();
      buffer.clear();
      LogUtil.logClientInfo("Responded from " + channel.getLocalAddress());
    } catch (PortUnreachableException e) {
      throw new NetworkException();
    } catch (IOException | ClassNotFoundException e) {
      LogUtil.logClientErrorWONotif(e);
      Printer.printError("Logged error occurred");
    }
    return response;
  }
}
