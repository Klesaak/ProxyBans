package ua.klesaak.proxybans.utils.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import ua.klesaak.proxybans.utils.messages.Message;

public class AbstractCommandException extends Exception {

    public AbstractCommandException(CommandSender commandSender, String message) {
        commandSender.sendMessage(TextComponent.fromLegacyText(message));
    }

    public AbstractCommandException(CommandSender commandSender, BaseComponent[] message) {
        commandSender.sendMessage(message);
    }

    public AbstractCommandException(CommandSender commandSender, Message message) {
        message.send(commandSender);
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
