package ua.klesaak.proxybans.permshook;

import lombok.val;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ProxyServer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public final class LuckPermsHook implements IPermHook {
    private final LuckPerms luckPermsApi = LuckPermsProvider.get();

    public LuckPermsHook() {
    }

    @Override
    public String getUserGroup(String nickName) {
        String group = "";
        try {
            group = this.getGroup(nickName);
        } catch (InterruptedException | ExecutionException exception) {
            throw new RuntimeException("Ошибка при получении группы из LuckPerms", exception);
        }
        return group;
    }

    @Override
    public boolean hasPermission(String nickName, String permission) {
        return false;//я ебал этот апи
    }

    private String getLPGroup(String nick) throws InterruptedException, ExecutionException {
        val userManager = this.luckPermsApi.getUserManager();
        CompletableFuture<UUID> uuid = userManager.lookupUniqueId(nick);
        val userId = uuid.get();
        if (userId != null) {
            CompletableFuture<User> user = userManager.loadUser(userId);
            return user.get().getPrimaryGroup();
        }
        return "default";
    }

    private String getGroup(String nick) throws InterruptedException, ExecutionException {
        val proxiedPlayer = ProxyServer.getInstance().getPlayer(nick);
        if (proxiedPlayer != null) {
            User user = this.luckPermsApi.getUserManager().getUser(proxiedPlayer.getName());
            return user != null ? user.getPrimaryGroup() : getLPGroup(nick);
        }
        return "default";
    }
}
