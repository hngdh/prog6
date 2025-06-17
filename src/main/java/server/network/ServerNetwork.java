package server.network;

import common.dataProcessors.InputReader;
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
import server.ioStream.Handler;

public class ServerNetwork {
  private final int MAX_PACKET_SIZE = 65536;
  private final ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
  private final InputReader reader = new InputReader();
  private int PORT = 404;
  private Handler handler;
  private Selector selector;
  private boolean isAwaken;

  public ServerNetwork() {
    reader.setReader();
    isAwaken = true;
  }

  public void init(String fileName) throws LogException {
    LogUtil.logServerInfo("Server started");
    handler = new Handler();
    handler.prepare(fileName);
    establish();
  }

  private void establish() throws LogException {
    try (DatagramChannel channel = DatagramChannel.open()) {
      channel.socket().bind(new InetSocketAddress("127.0.0.1", PORT));
      channel.configureBlocking(false);
      selector = Selector.open();
      channel.register(selector, SelectionKey.OP_READ);
      handle();
    } catch (IOException | LogException e) {
      Printer.printError(
          "Error establish connection on port " + PORT + ", please choose another port");
      portResolve();
    }
  }

  private void portResolve() throws LogException {
    try {
      int port = Integer.parseInt(reader.readLine());
      if (port > 65535 || port < 0) {
        Printer.printError("Port number can't exceed range [0-65535]");
        portResolve();
      } else {
        PORT = port;
        establish();
      }
    } catch (IOException | NumberFormatException f) {
      Printer.printError("Wrong input/ number format");
      portResolve();
    }
  }

  public void handle() throws LogException, IOException {
    LogUtil.logServerInfo("Listening on port " + PORT);
    while (isAwaken) {
      if (selector.select(1) == 0) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if (br.ready()) {
          String input = br.readLine();
          handler.processServer(input);
        }
      }
      for (SelectionKey key : selector.selectedKeys()) {
        if (key.channel() instanceof DatagramChannel dc) {
          Response response;
          Request request = new Request("", "", null);
          try {
            request = readRequest(dc);
            response = handler.processClient(request);
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
    handler.processClient(new Request("save", null, null));
    isAwaken = false;
  }

  public Request readRequest(DatagramChannel dc) throws LogException {
    try {
      SocketAddress remoteAddress = dc.receive(buffer);
      Printer.printCondition("received");
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
      ByteBuffer buf = ByteBuffer.wrap(baos.toByteArray());
      channel.send(buf, port);
      LogUtil.logServerInfo("Response sent to " + port);
    } catch (IOException e) {
      LogUtil.logServerError(e);
      Printer.printError("Error during sending process");
      throw new LogException();
    }
  }
}
