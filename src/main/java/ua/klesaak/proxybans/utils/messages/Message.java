package ua.klesaak.proxybans.utils.messages;

import lombok.val;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message implements Cloneable {
    private final String message;
    private final boolean isList;
    private final boolean isWithoutQuotes;

    public Message(String message, boolean isList, boolean isWithoutQuotes) {
        String mes = isWithoutQuotes ? message.replace("\"", "") : message;
        this.message = ChatColor.translateAlternateColorCodes('&', mes);
        this.isList = isList;
        this.isWithoutQuotes = isWithoutQuotes;
    }

    public void broadcast() {
        val components = this.getMessageComponent();
        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> proxiedPlayer.sendMessage(components));
        ProxyServer.getInstance().getConsole().sendMessage(components);
    }

    public static Message create(String text, boolean isList, boolean isWithoutQuotes) {
        return new Message(text, isList, isWithoutQuotes);
    }

    public void send(CommandSender... players) {
        val components = this.getMessageComponent();
        for (CommandSender player : players) {
            player.sendMessage(components);
        }
    }

    public Message tag(Pattern pattern, String replacement) {
        return create(this.replaceAll(this.message, pattern, ()-> replacement), this.isList, this.isWithoutQuotes);
    }

    public void send(CommandSender player) {
        player.sendMessage(this.getMessageComponent());
    }

    public void send(String playerName) {
        val proxiedPlayer = ProxyServer.getInstance().getPlayer(playerName);
        if (proxiedPlayer != null) this.send(proxiedPlayer);
    }

    public void disconnect(ProxiedPlayer proxiedPlayer) {
        if (proxiedPlayer != null && this.isList) {
            proxiedPlayer.disconnect(this.getMessageComponent());
        }
    }

    public BaseComponent[] getMessageComponent() {
        if (this.isWithoutQuotes || this.isList) {
            return TextComponent.fromLegacyText(this.message);
        }
        return ComponentSerializer.parse(this.message);
    }

    public String getMessageString() {
        return this.message;
    }

    private String replaceAll(String message, Pattern pattern, Supplier<String> replacement) {
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) return matcher.replaceAll(Matcher.quoteReplacement(replacement.get()));
        return message;
    }

    @Override
    public Message clone() {
        try {
            super.clone();
            return new Message(this.message, this.isList, this.isWithoutQuotes);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Got error while trying to clone Message", e);
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
    }
}
