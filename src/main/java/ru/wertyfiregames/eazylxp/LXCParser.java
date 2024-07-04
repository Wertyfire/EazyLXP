/**
 * File created on 17:19 03.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class LXCParser implements ILXParser {
    private final File lxc;
    private String language;
    private final Map<String, Map<String, String>> translations = new HashMap<>();
    private final Map<String, Integer> languagesIndex = new HashMap<>();
    protected LXCParser(File file, String defaultLanguage) {
        lxc = file;

        language = defaultLanguage;

        try {
            processStrings();
        } catch (LXCSyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void processStrings() throws LXCSyntaxException {
        try (BufferedReader reader = new BufferedReader(new FileReader(lxc))) {
            String header = reader.readLine();
            if (header == null)
                throw new LXCSyntaxException("XLC header is null!");

            String[] headerParts = header.split("\\|");
            for (int i = 1; i < headerParts.length; i++) {
                languagesIndex.put(headerParts[i], i);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != headerParts.length)
                    throw new LXCSyntaxException("Line '" + line + "' format does not equals to header '" + header + "'.");

                String key = parts[0];
                Map<String, String> translationMap = new HashMap<>();
                for (int i = 1; i < parts.length; i++) {
                    translationMap.put(headerParts[i], parts[i]);
                }
                translations.put(key, translationMap);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read lxc file! Error:\n" + e.getMessage());
        }
    }

    public void setLanguage(String newLanguage) {
        language = newLanguage;
    }

    public String get(String key) {
        Map<String, String> translationsMap = translations.get(key);
        if (translationsMap != null) {
            String translation = translationsMap.get(language);
            if (translation != null)
                return translation;
        }
        return key;
    }
}