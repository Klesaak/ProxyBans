package ua.klesaak.proxybans.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class JacksonAPI {
    private final ObjectMapper EMPTY_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    public final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    @SneakyThrows
    public <T> T readFile(File file, Class<T> clazz) {
        return OBJECT_MAPPER.readValue(file, clazz);
    }

    @SneakyThrows
    public <T> T readPath(Path path, Class<T> clazz) {
        return OBJECT_MAPPER.readValue(readPath(path), clazz);
    }

    @SneakyThrows
    public <T> T readValue(String json, Class<T> clazz) {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    @SneakyThrows
    public String readFile(File file) {
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public String readPath(Path path) {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    public Path writeFile(File file, Object value) {
        return writeFile(file, readAsString(value));
    }

    @SneakyThrows
    public Path writeFile(File file, String value) {
        return Files.write(file.toPath(), value.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public Path writePath(Path path, Object value) {
        return writePath(path, readAsString(value));
    }

    @SneakyThrows
    public Path writePath(Path path, String value) {
        return Files.write(path, value.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public <K, V> Map<K, V> fastMap(List<K> keys, List<V> values) {
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < keys.size(); ++i) {
            map.put(keys.get(i), values.get(i));
        }
        return map;
    }

    @SneakyThrows
    public String readAsString(Object value) {
        return OBJECT_MAPPER.writeValueAsString(value);
    }
}
