package br.com.marvel.utils;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceLoader {

    public static String loadFile(String file) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream is = classLoader.getResourceAsStream(file)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            return null;
        }
    }

}
