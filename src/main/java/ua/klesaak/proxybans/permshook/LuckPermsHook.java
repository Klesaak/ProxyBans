package ua.klesaak.proxybans.permshook;

import lombok.val;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import ua.klesaak.proxybans.ProxyBansPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public final class LuckPermsHook implements IPermHook {
    private final LuckPerms luckPermsApi = LuckPermsProvider.get();

    public LuckPermsHook(ProxyBansPlugin proxyBansPlugin) {
        proxyBansPlugin.getLogger().info("Loaded permission provider: " + this.getClass().getSimpleName());
    }

    @Override
    public String getUserGroup(String nickName) {
        return this.getGroup(nickName);
    }

    @Override
    public boolean hasPermission(String nickName, String permission) {
        return false;//я ебал этот апи
    }

    private String loadLPGroup(String nick) {
        val userManager = this.luckPermsApi.getUserManager();
        String group = "default";
        try {
            CompletableFuture<UUID> uuidLoadTask = userManager.lookupUniqueId(nick);
            UUID userId = uuidLoadTask.get();
            if (userId != null) {
                CompletableFuture<User> userLoadTask = userManager.loadUser(userId);
                group = userLoadTask.get().getPrimaryGroup();
            }
        } catch (ExecutionException | InterruptedException exception) {
            throw new RuntimeException("Произошла ошибка при получении группы игрока из LuckPerms.", exception);
        }
        return group;
    }

    private String getGroup(String nick) {
        User user = this.luckPermsApi.getUserManager().getUser(nick);
        return user != null ? user.getPrimaryGroup() : this.loadLPGroup(nick);
    }
}
