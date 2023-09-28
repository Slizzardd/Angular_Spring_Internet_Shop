package ua.com.alevel.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("loggerService")
public class LogbackLoggerService implements LoggerService {

    private static final Logger LOGGER_INFO = LoggerFactory.getLogger(LoggerLevel.INFO.getLevel());
    private static final Logger LOGGER_WARN = LoggerFactory.getLogger(LoggerLevel.WARN.getLevel());
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger(LoggerLevel.ERROR.getLevel());

    @Override
    public void commit(LoggerLevel level, String message) {
        switch (level) {
            case INFO:
                LOGGER_INFO.info(message);
                break;
            case WARN:
                LOGGER_WARN.warn(message);
                break;
            case ERROR:
                LOGGER_ERROR.error(message);
                break;
        }
    }

}
