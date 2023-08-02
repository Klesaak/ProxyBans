package ua.klesaak.proxybans.utils.messages;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message implements Cloneable {
    private final String message;

    public Message(String message) {
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    public void broadcast() {
        ProxyServer.getInstance().getPlayers().forEach(this::send);
        this.send(ProxyServer.getInstance().getConsole());
    }

    public static Message create(String text) {
        return new Message(text);
    }

    public void send(CommandSender... players) {
        for (CommandSender player : players) {
            this.send(player);
        }
    }

    public Message tag(Pattern pattern, String replacement) {
        return new Message(this.replaceAll(this.message, pattern, ()-> replacement));
    }

    public void send(CommandSender player) {
        player.sendMessage(ComponentSerializer.parse(this.message));
    }

    private String replaceAll(String message, Pattern pattern, Supplier<String> replacement) {
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) return matcher.replaceAll(Matcher.quoteReplacement(replacement.get()));
        return message;
    }

    public BaseComponent[] getMessageComponent() {
        return TextComponent.fromLegacyText(this.message);
    }

    public String getMessageString() {
        return this.message;
    }

    @Override
    public Message clone() {
        try {
            return (Message)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
    }
}
