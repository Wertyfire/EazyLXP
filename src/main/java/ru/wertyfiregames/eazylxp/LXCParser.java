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
     * Loaded translation mapped as {@code Map<String key, Map<String language, String value>>}
     * @since 1.0
     * @see #languagesIndex
     * */
    private final Map<String, Map<String, String>> translations = new HashMap<>();
    /**
     * Column indices of language from the LXC <em>header</em> (see package documentation for details).
     * @since 1.0
     * @see #translations
     * */
    private final Map<String, Integer> languagesIndex = new HashMap<>();

    /**
     * Private constructor to prevent direct instantiation without parameters.
     * @since 1.0
     * @see #LXCParser(File, String)
     * */
    private LXCParser() {
        lxc = null;
    }

    /**
     * Constructs a new {@code LXCParser} instance and immediately parses the specified file.
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
            if (version != LATEST_LXC_VERSION)
                throw new LXCSyntaxException("Unsupported LXC version: " + version + ". Expected version: " + LATEST_LXC_VERSION + ".");

            String header = readLine(reader);
            if (header == null)
                throw new LXCSyntaxException("Missing LXC header: expected column definitions after the version line.");

            String[] headerParts = header.split("\\|");
            for (int i = 1; i < headerParts.length; i++) {
                languagesIndex.put(headerParts[i], i);
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
     * @return the column index of {@code language}.
     * @since 1.0
     * */
    public int getLanguageIndex(String language) {
        return languagesIndex.get(language);
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
}