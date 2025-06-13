import common.exceptions.LogException;
import server.network.ServerNetwork;

import java.io.IOException;

/**
 * The {@code Server} class serves as the entry point for the application. It initializes and starts
 * the application's core components.
 */
public class Server {
    public static void main(String[] args) throws LogException, IOException {
        String fileName = "src\\main\\resources\\server\\data.csv";
        ServerNetwork serverNetwork = new ServerNetwork();
        Runtime.getRuntime().addShutdownHook(new Thread(serverNetwork::shutdown));
        serverNetwork.init(fileName);
    }
}
