package eco.ywhc.xr.rest.config.spy.appender;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.FormattedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Appender which delegates to SLF4J. All log messages are logged at the INFO
 * level using the "p6spy" category, except debug and error ones that log on the
 * respective SLF4J categories.
 */
public class SpySlf4JLogger extends FormattedLogger {

    private final Logger log;

    public SpySlf4JLogger() {
        log = LoggerFactory.getLogger(SpySlf4JLogger.class);
    }

    @Override
    public void logException(Exception e) {
        log.warn("", e);
    }

    @Override
    public void logText(String text) {
        if (text == null || text.trim().isBlank()) {
            log.debug(text);
        }
    }

    @Override
    public void logSQL(int connectionId, String now, long elapsed,
                       Category category, String prepared, String sql, String url) {
        final String msg = strategy.formatMessage(connectionId, now, elapsed,
                category.toString(), prepared, sql, url);
        if (msg == null || msg.trim().isBlank()) {
            return;
        }
        if (Category.ERROR.equals(category)) {
            log.error(msg);
        } else if (Category.WARN.equals(category)) {
            log.warn(msg);
        } else if (Category.DEBUG.equals(category)) {
            log.debug(msg);
        } else {
            log.debug(msg);
        }
    }

    @Override
    public boolean isCategoryEnabled(Category category) {
        if (Category.ERROR.equals(category)) {
            return log.isErrorEnabled();
        } else if (Category.WARN.equals(category)) {
            return log.isWarnEnabled();
        } else if (Category.DEBUG.equals(category)) {
            return log.isDebugEnabled();
        } else {
            return log.isInfoEnabled();
        }
    }

}
