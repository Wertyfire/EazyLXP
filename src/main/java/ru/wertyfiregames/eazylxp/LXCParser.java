/**
 * File created on 17:19 03.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser for {@code .lxc} and {@code .lexc} files (identical formats).
 * @since 1.0
 * @author Wertyfire
 * */
public class LXCParser implements ILXParser {
    /**
     * {@code File} object of lxc file.
     * @since 1.0
     * */
    private final File lxc;
    /**
     * The currently active language. Can be set in the constructor or changed via {@link #setLanguage(String)}.
     * @since 1.0
     * @see #setLanguage(String)
     * */
    private String language;
    /**
     * Latest supported version of lxc.
     * @since 1.0
     * */
    public final int LATEST_LXC_VERSION = 3;
    /**
     * Loaded translations mapped as {@code Map<String key, Map<String language, String value>>}
     * @since 1.0
     * @see #languageIndices
     * */
    private final Map<String, Map<String, String>> translations = new HashMap<>();
    /**
     * Column indices of language from the LXC <em>header</em> (see package documentation for details).
     * @since 1.0
     * @see #translations
     * */
    private final Map<String, Integer> languageIndices = new HashMap<>();

    /**
     * Private constructor to prevent direct instantiation without parameters.
     * @since 1.0
     * @see #LXCParser(File, String)
     * */
    private LXCParser() {
        lxc = null;
    }

    /**
     * Constructs a new {@code LXCParser} instance and immediately parses the specified file if it exists and is not empty.
     * <p>If {@code file} does not exist yet or is empty ({@code 0} bytes), the parser is initialized empty so translations
     * can be added via {@link #set(String, String...)} and written later using {@link #save()}.
     * Unlike {@link LocalizationFile}, which suppresses exceptions, this parser throws an
     * {@link LXCSyntaxException} if the file syntax is invalid.
     * @param file the {@code .lxc} or {@code .lexc} file to parse.
     * @param defaultLanguage the initial language to use. Can be changed later via {@link #setLanguage(String)}.
     * @throws IOException if an I/O error occurs while reading the file.
     * @throws LXCSyntaxException if the file contains invalid LXC syntax.
     * @since 1.0
     * @see LocalizationFile
     * */
    public LXCParser(File file, String defaultLanguage) throws IOException, LXCSyntaxException {
        lxc = file;
        language = defaultLanguage;

        if (file != null && file.length() > 0)
            processStrings();
    }

    /**
     * Parse all lines in localization file. If syntax invalid, throws {@code LXCSyntaxException}.
     * @throws IOException if an I/O error occurs while reading the file.
     * @throws LXCSyntaxException if lxc syntax is invalid.
     * @since 1.0
     * */
    private void processStrings() throws IOException, LXCSyntaxException {
        try (BufferedReader reader = Files.newBufferedReader(lxc.toPath(), StandardCharsets.UTF_8)) {
            String v = readLine(reader);
            if (v == null ||!v.startsWith("!version "))
                throw new LXCSyntaxException("Invalid LXC syntax: file must start with '!version '.");
            int version;
            try {
                version = Integer.parseInt(v.substring(v.indexOf("n") + 2));
            } catch (NumberFormatException e) {
                throw new LXCSyntaxException("Invalid LXC version format: '" + v + "'.", e);
            }
            if (version < 1 || version > LATEST_LXC_VERSION)
                throw new LXCSyntaxException("Unsupported LXC version: " + version + ". Expected version: " + LATEST_LXC_VERSION + ".");

            String header = readLine(reader);
            if (header == null)
                throw new LXCSyntaxException("Missing LXC header: expected column definitions after the version line.");

            String[] headerParts = header.split("\\|");
            for (int i = 1; i < headerParts.length; i++) {
                languageIndices.put(headerParts[i], i);
            }

            String line;

            while ((line = readLine(reader)) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length != headerParts.length)
                    throw new LXCSyntaxException("Line '" + line + "' does not match the header format '" + header + "'.");

                String key = parts[0];
                Map<String, String> translationMap = new HashMap<>();
                for (int i = 1; i < parts.length; i++) {
                    translationMap.put(headerParts[i], parts[i]);
                }
                translations.put(key, translationMap);
            }
        }
    }

    /**
     * Reads the next non-empty line from the reader, stripping out any comments.
     * @param reader an initialized {@code BufferedReader}.
     * @return the next valid line, or {@code null} if the end of the stream is reached.
     * @throws IOException if an I/O error occurs while reading.
     * @since 1.1
     * */
    private String readLine(BufferedReader reader) throws IOException {
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty())
                continue;

            if (line.startsWith("#"))
                continue;

            int hashIndex = line.indexOf('#');
            if (hashIndex != -1)
                line = line.substring(0, hashIndex).trim();

            return line;
        }

        return null;
    }

    /**
     * Merges translations and languages from another {@code LXCParser} into this instance.
     * <p>Any languages present in {@code parser} that do not exist in this instance will be
     * added automatically. If {@code parser} is {@code null} or refers to the same instance,
     * no changes are made.
     * @param parser the {@code LXCParser} whose translations will be merged into this parser.
     * @param prioritizeNew {@code true} to overwrite existing translations when keys and languages
     *                                  collide; {@code false} to preserve existing translations and
     *                                  only add missing keys or languages.
     * @return this {@code LXCParser} instance after merging.
     * @since 1.3
     * */
    public LXCParser merge(LXCParser parser, boolean prioritizeNew) {
        if (parser == null || this == parser) return this;

        for (String lang : parser.languageIndices.keySet()) {
            if (!languageIndices.containsKey(lang))
                languageIndices.put(lang, languageIndices.size() + 1);
        }

        for (Map.Entry<String, Map<String, String>> entry : parser.translations.entrySet()) {
            String key = entry.getKey();
            Map<String, String> incoming = entry.getValue();
            Map<String, String> existing = translations.get(key);

            if (existing == null)
                translations.put(key, new HashMap<>(incoming));
            else {
                for (Map.Entry<String, String> langEntry : incoming.entrySet()) {
                    if (prioritizeNew) existing.put(langEntry.getKey(), langEntry.getValue());
                    else existing.putIfAbsent(langEntry.getKey(), langEntry.getValue());
                }
            }
        }

        return this;
    }

    /**
     * Sets the current active language to {@code newLanguage}.
     * @param newLanguage the language name as defined in the file header.
     * @since 1.0
     * */
    public void setLanguage(String newLanguage) {
        language = newLanguage;
    }

    /**
     * Returns the column index of the specified {@code language} in the file <em>header</em> (starting from 1).
     * @param language language name from <em>header</em>.
     * @return the column index of {@code language}, or {@code -1} if the language is not found.
     * @since 1.0
     * */
    public int getLanguageIndex(String language) {
        return languageIndices.getOrDefault(language, -1);
    }

    /**
     * Retrieves the translated value for the specified {@code key} in the current language.
     * @param key the translation key from the file.
     * @return the translated string for {@code key}, or {@code key} itself if no translation is found.
     * @since 1.0
     * */
    public String get(String key) {
        Map<String, String> translationsMap = translations.get(key);
        if (translationsMap != null) {
            String translation = translationsMap.get(language);
            if (translation != null)
                return translation;
        }
        return key;
    }

    /**
     * Sets or updates translations for the specified {@code key} across one or more languages.
     * <p>The {@code values} varargs must contain alternating language names and translated strings
     * (e.g., {@code "english", "Hello World!", "russian", "Привет, мир!"}). Any languages not yet present in
     * the parser will be added automatically.
     * @param key the translation key to add or update.
     * @param values alternating pairs of language names and their corresponding translations.
     * @return this {@code LXCParser} instance after updating translations.
     * @since 1.3
     * */
    public ILXParser set(String key, String... values) {
        if (key == null || values == null)
            throw new IllegalArgumentException("Key and values must not be null.");

        if (values.length % 2 != 0)
            throw new IllegalArgumentException("Values must be provided in 'language, translation' pairs (even length).");

        for (int i = 0; i < values.length; i += 2) {
            String lang = values[i];
            String translation = values[i + 1];
            if (lang == null || translation == null)
                throw new IllegalArgumentException("Language and translation values must not be null.");

            languageIndices.putIfAbsent(lang, languageIndices.size() + 1);
            translations.computeIfAbsent(key, ignored -> new HashMap<>()).put(lang, translation);
        }

        return this;
    }

    /**
     * Saves all loaded translations back to the original {@code .lxc} or {@code .lexc} file.
     * @throws IOException if an I/O error occurs while writing to the file.
     * @since 1.3
     * @see #saveTo(File)
     * */
    public void save() throws IOException {
        saveTo(lxc);
    }

    /**
     * Saves all loaded translations to the specified target {@code file} in UTF-8 encoding.
     * @param file the destination {@code .lxc} or {@code .lexc} file to write to.
     * @throws IOException if an I/O error occurs while writing to the file.
     * @since 1.3
     * @see #save()
     * */
    public void saveTo(File file) throws IOException {
        StringBuilder output = new StringBuilder();
        output.append("!version ").append(LATEST_LXC_VERSION).append("\n");

        String[] orderedLangs = new String[languageIndices.size()];
        for (Map.Entry<String, Integer> entry : languageIndices.entrySet())
            orderedLangs[entry.getValue() - 1] = entry.getKey();

        output.append("key");
        for (String lang : orderedLangs) output.append("|").append(lang);
        output.append("\n");

        for (Map.Entry<String, Map<String, String>> entry : translations.entrySet()) {
            String key = entry.getKey();
            Map<String, String> translations = entry.getValue();

            output.append("\n").append(key);
            for (String lang : orderedLangs)
                output.append("|").append(translations.getOrDefault(lang, ""));
        }

        Files.writeString(file.toPath(), output.toString(), StandardCharsets.UTF_8);
    }
}