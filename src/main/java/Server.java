import common.exceptions.LogException;
import java.io.IOException;
import server.network.ServerNetwork;

/**
 * The {@code Server} class serves as the entry point for the application. It initializes and starts
 * the application's core components.
 */
public class Server {
  public static void main(String[] args) throws LogException, IOException {
    String fileName = "server/data.csv";
    ServerNetwork serverNetwork = new ServerNetwork();
    Runtime.getRuntime().addShutdownHook(new Thread(serverNetwork::shutdown));
    serverNetwork.init(fileName);
  }
}
