package client.network;

import common.exceptions.NetworkException;
import common.exceptions.ServerOfflineException;
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
  private final int MAX_PACKET_SIZE = 65456;
  private int PORT = 4004;
  private DatagramChannel channel;
  private SocketAddress serverAddress;

  public ClientNetwork() {
    ping(45);
  }

  public void connect() {
    try {
      channel = DatagramChannel.open();
      serverAddress = new InetSocketAddress("localhost", PORT);
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
      Response response = getResponse(channel, MAX_PACKET_SIZE);
      shutdown();
      return response;
    } catch (NetworkException e) {
      ping(45);
    }
    return null;
  }

  public void ping(int count) {
    Request request = new Request("ping", null, null);
    try {
      connect();
      sendPacket(channel, serverAddress, request);
      getResponse(channel, MAX_PACKET_SIZE);
      Printer.printInfo("Connected on port " + PORT);
      shutdown();
    } catch (NetworkException e) {
      try {
        if (PORT < 49049) {
          PORT += 1001;
        } else {
          PORT = 4004;
        }
        if (count == 0) throw new ServerOfflineException();
        ping(--count);
      } catch (ServerOfflineException f) {
        Printer.printError(f);
      }
    }
  }

  private void portResolve() {}

  public void sendPacket(DatagramChannel channel, SocketAddress serverAddress, Request request) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(request);
      oos.flush();
      ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
      channel.send(buffer, serverAddress);
      LogUtil.logClientInfo(
          "Sending '" + request.getCommand() + "' from " + channel.getLocalAddress());
    } catch (IOException e) {
      LogUtil.logClientErrorWONotif(e);
    }
  }

  public Response getResponse(DatagramChannel channel, int MAX_PACKET_SIZE) {
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
      LogUtil.logClientErrorWONotif(e);
      throw new NetworkException();
    } catch (IOException | ClassNotFoundException e) {
      LogUtil.logClientErrorWONotif(e);
      Printer.printError("Logged error occurred");
    }
    return response;
  }
}
