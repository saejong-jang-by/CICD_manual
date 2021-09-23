package dao;

import handler.HandlerApiGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocDBRestApiException extends RuntimeException {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerApiGateway.class);

    private static final long serialVersionId = 1L;

    public DocDBRestApiException(String message) {
        super(message);
        LOGGER.error(message);
    }

    public DocDBRestApiException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.error(message, cause);
    }
}
