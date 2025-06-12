import common.exceptions.LogException;
import server.network.ServerNetwork;

/**
 * The {@code Server} class serves as the entry point for the application. It initializes and starts
 * the application's core components.
 */
public class Server {
    public static void main(String[] args) throws LogException {
        String fileName = "src\\main\\resources\\server\\data.csv";
        ServerNetwork serverNetwork = new ServerNetwork();
        serverNetwork.init(fileName);
    }
}
