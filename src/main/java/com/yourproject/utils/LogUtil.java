package com.yourproject.utils;

import org.slf4j.Logger;
import java.util.Map;

public class LogUtil {
    private LogUtil() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static void logInfo(Logger logger, String message, Map<String, Object> context) {
        if (context == null || context.isEmpty()) {
            logger.info(message);
        } else {
            logger.info("{} - Context: {}", message, context);
        }
    }

    public static void logWarn(Logger logger, String message, Map<String, Object> context) {
        if (context == null || context.isEmpty()) {
            logger.warn(message);
        } else {
            logger.warn("{} - Context: {}", message, context);
        }
    }

    public static void logError(Logger logger, String message, Exception exception) {
        logger.error(message, exception);
    }

    public static void logError(Logger logger, String message, Exception exception, Map<String, Object> context) {
        if (context == null || context.isEmpty()) {
            logger.error(message, exception);
        } else {
            logger.error("{} - Context: {} - Exception: {}", message, context, exception.getMessage(), exception);
        }
    }
}
