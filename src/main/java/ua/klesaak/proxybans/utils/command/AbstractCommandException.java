package ua.klesaak.proxybans.utils.command;

import lombok.Getter;
import ua.klesaak.proxybans.utils.messages.Message;

@Getter
public class AbstractCommandException extends Exception {
    private Message minecraftMessage;

    public AbstractCommandException(Message message) {
        this.minecraftMessage = message;
    }

    protected AbstractCommandException(String message) {
        super(message);
    }

    protected AbstractCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AbstractCommandException(Throwable cause) {
        super(cause);
    }

    protected AbstractCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }
}
