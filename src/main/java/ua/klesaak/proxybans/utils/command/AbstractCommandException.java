package ua.klesaak.proxybans.utils.command;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.utils.messages.Message;

@Getter
public class AbstractCommandException extends RuntimeException {
    private final Message minecraftMessage;

    public AbstractCommandException(Message message) {
        super("Message should be used instead.", null, false, false);
        this.minecraftMessage = message;
    }

    public void sendMinecraftMessage(CommandSender commandSender) {
        if (this.minecraftMessage != null) this.minecraftMessage.send(commandSender);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
