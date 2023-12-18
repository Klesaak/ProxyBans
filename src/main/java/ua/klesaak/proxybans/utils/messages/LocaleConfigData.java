package ua.klesaak.proxybans.utils.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import lombok.SneakyThrows;
import lombok.val;
import ua.klesaak.proxybans.utils.jackson.JacksonAPI;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public interface LocaleConfigData<T extends LocaleConfigData<T>> {

    @SneakyThrows @SuppressWarnings("unchecked")
    default T load(File file) { // TODO: 14.06.2023 убрать говнокод...
        if (!file.getParentFile().exists()) Files.createDirectory(file.getParentFile().toPath());
        if (!file.exists()) {
            Files.createFile(file.toPath());
            Files.write(file.toPath(), "{}".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        JsonNode jsonNode = JacksonAPI.readPath(file.toPath(), JsonNode.class);
        val thisClass = getClass();

        val keyList = new ArrayList<String>();
        for (val field : getFields(thisClass)) { //собирает коллекцию ключей
            if (field.isAnnotationPresent(MessageField.class)) {
                val data = field.getAnnotation(MessageField.class);
                val messageKey = data.key().isEmpty() ? field.getName() : data.key();
                keyList.add(messageKey);
            }
        }

        if (keyList.stream().anyMatch(key -> !jsonNode.has(key))) { //записывает дефолт файл если ключей нет
            LinkedHashMap<String, JsonNode> map = new LinkedHashMap<>();
            for (val field : getFields(thisClass)) {
                if (field.isAnnotationPresent(MessageField.class)) {
                    val data = field.getAnnotation(MessageField.class);
                    val messageKey = data.key().isEmpty() ? field.getName() : data.key();
                    val message = data.defaultMessage().length > 1 ? Arrays.asList(data.defaultMessage()) : data.defaultMessage()[0];
                    map.put(messageKey, JacksonAPI.OBJECT_MAPPER.valueToTree(message));
                }
            }
            JacksonAPI.writeFile(file, map);
        }

        JsonNode newJsonNode = JacksonAPI.readPath(file.toPath(), JsonNode.class);
        for (val field : getFields(thisClass)) { //по новой загружает поля
            if (field.isAnnotationPresent(MessageField.class)) {
                val data = field.getAnnotation(MessageField.class);
                val messageKey = data.key().isEmpty() ? field.getName() : data.key();
                boolean isWithoutQuotes = data.withoutQuotes();
                val nodeKey = newJsonNode.get(messageKey);
                boolean isList = nodeKey.isArray();
                val message = isList ? Joiner.on('\n').join(JacksonAPI.readValue(nodeKey.toString(), List.class)) : nodeKey.toString();
                field.setAccessible(true);
                field.set(this, new Message(message, isList, isWithoutQuotes));
            }
        }

        return (T) this;
    }

    default List<Field> getFields(Class<?> clazz) {
        val parentClass = (Class<?>) clazz.getSuperclass();

        val fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        if (parentClass != null && parentClass != Object.class) fields.addAll(getFields(parentClass));

        return fields;
    }
}
