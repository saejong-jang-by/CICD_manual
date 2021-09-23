package utils;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerUtil.class);

    private LoggerUtil() {
    }

    public static void logEnvironment(Object event, Context context, Gson gson) {
        // log execution details
        LOGGER.debug("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
        LOGGER.debug("CONTEXT: " + gson.toJson(context));
        // log event details
        LOGGER.debug("EVENT: " + gson.toJson(event));
        LOGGER.debug("EVENT TYPE: " + event.getClass().toString());
    }
}
