package ua.klesaak.proxybans.utils;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class UtilityMethods {

    public String replaceAll(String message, Pattern pattern, Supplier<String> replacement) {
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) return matcher.replaceAll(Matcher.quoteReplacement(replacement.get()));
        return message;
    }
}
