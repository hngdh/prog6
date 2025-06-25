import common.exceptions.LogException;
import java.io.IOException;
import server.network.ServerNetwork;

/**
 * The {@code Server} class serves as the entry point for the application. It initializes and starts
 * the application's core components.
 */
public class Server {
  public static void main(String[] args) throws LogException, IOException {
    String port;
    String fileName = "server/data.csv";
    if (args.length != 0) {
      port = args[0];
    } else {
      port = "4004";
    }
    ServerNetwork serverNetwork = new ServerNetwork(port);
    Runtime.getRuntime().addShutdownHook(new Thread(serverNetwork::shutdown));
    serverNetwork.init(fileName);
  }
}
