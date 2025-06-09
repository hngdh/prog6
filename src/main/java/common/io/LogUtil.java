package common.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code LogUtil} class provides utility methods for logging exceptions.
 */
public class LogUtil {
    private static final Logger log = LoggerFactory.getLogger(LogUtil.class);

    private LogUtil() {
    }

    public static void log(Exception e) {
        log.error("", e);
    }
}
