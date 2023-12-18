package ua.klesaak.proxybans.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import ua.klesaak.proxybans.utils.jackson.JacksonAPI;

import java.io.File;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class JsonData {
    private transient File file;

    @SneakyThrows
    public JsonData(File file) {
        if (!file.getParentFile().exists()) Files.createDirectory(file.getParentFile().toPath());
        if (!file.exists()) {
            Files.createFile(file.toPath());
            this.file = file;
            return;
        }
        this.file = file;
    }

    public JsonData() {
    }

    @SneakyThrows
    public <T> T readAll(Class<? extends T> clazz) {
        return JacksonAPI.readPath(this.file.toPath(), clazz);
    }

    @SafeVarargs
    public static <T, V> Map<T, V> mapOf(Pair<T, V>... pairs) {
        val map = new HashMap<T, V>();
        for (Pair<T, V> pair : pairs) {
            map.putIfAbsent(pair.key, pair.value);
        }
        return map;
    }

    @SneakyThrows
    public static <T extends JsonData> T load(File file, Class<T> clazz) {
        if (file.exists() && file.length() != 0L) {
            T storage = JacksonAPI.readFile(file, clazz);
            storage.setFile(file);
            return storage;

        }
        if (!file.getParentFile().exists()) Files.createDirectory(file.getParentFile().toPath());
        if (!file.exists()) Files.createFile(file.toPath());
        T storage = clazz.newInstance();
        storage.setFile(file);
        storage.write(storage);
        return storage;
    }

    public static <T, V> Pair<T, V> pairOf(T key, V value) {
        return new Pair<>(key, value);
    }

    @SneakyThrows
    public <T> void write(T source) {
        JacksonAPI.writeFile(this.file, source);
    }

    @SneakyThrows
    public void reloadFields() {
        val clazz = this.getClass();
        val newClazz = load(this.file, clazz);
        for (val field : clazz.getDeclaredFields()) {
            val modifiers = field.getModifiers();
            if (Modifier.isTransient(modifiers)) continue;
            field.setAccessible(true);
            field.set(this, field.get(newClazz));
        }
    }

    @Getter
    public static class Pair<T, V> {
        private final T key;
        private final V value;

        public Pair(T key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

