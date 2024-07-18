package ua.klesaak.proxybans.utils.messages;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;
import ua.klesaak.proxybans.utils.MCColorUtils;
import ua.klesaak.proxybans.utils.UtilityMethods;

import java.util.regex.Pattern;

public class Message implements Cloneable {
    protected String message;
    private final boolean isList;
    private final boolean isWithoutQuotes;

    public Message(String message, boolean isList, boolean isWithoutQuotes) {
        String mes = isWithoutQuotes ? message.replace("\"", "") : message;
        this.message = MCColorUtils.color(mes);
        this.isList = isList;
        this.isWithoutQuotes = isWithoutQuotes;
    }

    public TagMessage tag(Pattern pattern, String replacement) {
        return new TagMessage(UtilityMethods.replaceAll(this.message, pattern, ()-> replacement), this.isList, this.isWithoutQuotes);
    }

    public void broadcast() {
        val components = this.getMessageComponent();
        ProxyServer proxyServer = ProxyServer.getInstance();
        proxyServer.getPlayers().forEach(proxiedPlayer -> proxiedPlayer.sendMessage(components));
        proxyServer.getConsole().sendMessage(components);
    }

    public void send(CommandSender... players) {
        val components = this.getMessageComponent();
        for (CommandSender player : players) {
            player.sendMessage(components);
        }
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
    public static class TagMessage extends Message {

        public TagMessage(String message, boolean isList, boolean isWithoutQuotes) {
            super(message, isList, isWithoutQuotes);
        }

        @Override
        public TagMessage tag(Pattern pattern, String replacement) {
            this.message = UtilityMethods.replaceAll(this.message, pattern, ()-> replacement);
            return this;
        }
    }
}
