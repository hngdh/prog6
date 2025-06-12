package common.io;

import common.exceptions.LogException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code LogUtil} class provides utility methods for logging exceptions.
 */
public class LogUtil {
    private static final Logger serverError = LoggerFactory.getLogger("server_error");
    private static final Logger serverInfo = LoggerFactory.getLogger("server_info");
    private static final Logger clientError = LoggerFactory.getLogger("client_error");
    private static final Logger clientInfo = LoggerFactory.getLogger("client_info");
    private static final Logger consoleInfo = LoggerFactory.getLogger("server_console");

    private LogUtil() {
    }

    public static void logServerError(Exception e) throws LogException {
        serverError.error("", e);
        throw new LogException();
    }

    public static void logServerInfo(String string) {
        serverInfo.info(string);
        consoleInfo.info(string);
    }

    public static void logClientError(Exception e) throws LogException {
        clientError.error("", e);
        throw new LogException();
    }

    public static void logClientInfo(String string) {
        clientInfo.info(string);
    }
}
