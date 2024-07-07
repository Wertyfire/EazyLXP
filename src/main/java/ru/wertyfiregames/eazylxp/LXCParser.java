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

/**
 * Parser for .lxc and .lexc (the same) files.
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
     * Current language. Can be changed in constructor or by calling method
     * {@link #setLanguage(String)}.
     * @since 1.0
     * @see #setLanguage(String)
     * */
    private String language;
    /**
     * Latest supported version of lxc.
     * @since 1.0
     * */
    public final int LATEST_LXC_VERSION = 2;
    /**
     * Translations from file. Map{String key, Map{String language, String value}}
     * @since 1.0
     * @see #languagesIndex
     * */
    private final Map<String, Map<String, String>> translations = new HashMap<>();
    /**
     * Indexes of language from <em>header</em> of lxc (See package info for more info).
     * @since 1.0
     * @see #translations
     * */
    private final Map<String, Integer> languagesIndex = new HashMap<>();

    /**
     * Private constructor so don't can instantiate empty class.
     * @since 1.0
     * @see #LXCParser(File, String)
     * */
    @SuppressWarnings("unused")
    private LXCParser() {
        lxc = null;
    }

    /**
     * Default constructor. Protected so instantiate can only from {@link LocalizationFile}. Set {@link #lxc}
     * to {@code file}, {@link #language} to {@code defaultLanguage} and call {@link #processStrings()}
     * @param file file from .lxc.
     * @param defaultLanguage default language. To change use {@link #setLanguage(String)}
     * @since 1.0
     * @see #LXCParser()
     * */
    protected LXCParser(File file, String defaultLanguage) {
        lxc = file;

        language = defaultLanguage;

        try {
            processStrings();
        } catch (LXCSyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Parse all lines in localization file. If syntax invalid, throws {@code LXCSyntaxException}.
     * @since 1.0
     * @throws LXCSyntaxException if lxc syntax is invalid.
     * */
    private void processStrings() throws LXCSyntaxException {
        try (BufferedReader reader = new BufferedReader(new FileReader(lxc))) {
            String v = reader.readLine();
            if (!v.startsWith("!version "))
                throw new LXCSyntaxException("Invalid syntax of lxc. File not starts with !version");
            int version = Integer.parseInt(v.substring(v.indexOf("n") + 2));
            if (version > 0 && version <= LATEST_LXC_VERSION - 1)
                throw new LXCSyntaxException("You trying to parse lxc file using version " + version + ", but last supported version is 2. Make sure that you entered correct version or update library to latest version.");

            String header = reader.readLine();
            if (header == null)
                throw new LXCSyntaxException("LXC header is null!");

            String[] headerParts = header.split("\\|");
            for (int i = 1; i < headerParts.length; i++) {
                languagesIndex.put(headerParts[i], i);
            }

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("#")) {
                    if (line.startsWith("#"))
                        continue;
                    else {
                        String[] translation = line.split("#");
                        line = translation[0];
                    }
                }
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
            throw new RuntimeException("Could not read lxc file! Error: " + e.getMessage());
        }
    }

    /**
     * Set current language to {@code newLanguage}.
     * @param newLanguage new language.
     * */
    public void setLanguage(String newLanguage) {
        language = newLanguage;
    }

    /**
     * Get index of language {@code language} from <em>header</em>. Can be used for debug.
     * @return index of {@code language}.
     * @param language language name from <em>header</em>.
     * @see #languagesIndex
     * */
    public int getLanguageIndex(String language) {
        return languagesIndex.get(language);
    }

    /**
     * Get value of {@code key} from file.
     * @return value of{@code key}.
     * @param key key of translation from file.
     * @see #translations
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
}