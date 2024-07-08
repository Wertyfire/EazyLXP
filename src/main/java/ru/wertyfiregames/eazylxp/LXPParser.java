/**
 * File created on 20:20 07.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser for .lxp and .lexp (the same) files.
 * @since 1.1
 * @author Wertyfire
 * */
public class LXPParser implements ILXParser {
    /**
     * {@code File} object of lxc file.
     * @since 1.1
     * */
    private final File lxp;
    /**
     * Current language. Can be changed in constructor or by calling method
     * {@link #setLanguage(String)}.
     * @since 1.1
     * @see #setLanguage(String)
     * */
    private String language;
    /**
     * Latest supported version of lxp.
     * @since 1.1
     * */
    public final int LATEST_LXP_VERSION = 1;
    /**
     * Translations from file. Map{String key, Map{String language, String value}}
     * @since 1.1
     * @see #languagesIndex
     * */
    private final Map<String, Map<String, String>> translations = new HashMap<>();
    /**
     * Indexes of language from <em>header</em> of lxc (See package info for more info).
     * @since 1.1
     * @see #translations
     * */
    private final Map<String, Integer> languagesIndex = new HashMap<>();
    private final Map<String, Object> defaultModules = new HashMap<>();

    /**
     * Private constructor so don't can instantiate empty class.
     * @since 1.1
     * @see #LXPParser(File, String)
     * */
    @SuppressWarnings("unused")
    private LXPParser() {
        lxp = null;
    }

    /**
     * Default constructor. Protected so instantiate can only from {@link LocalizationFile}. Set {@link #lxp}
     * to {@code file}, {@link #language} to {@code defaultLanguage} and call {@link #processStrings()}
     * @param file file from .lxc.
     * @param defaultLanguage default language. To change use {@link #setLanguage(String)}
     * @since 1.1
     * @see #LXPParser()
     * */
    public LXPParser(File file, String defaultLanguage) {
        lxp = file;

        language = defaultLanguage;

        try {
            processStrings();
        } catch (LXPSyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Parse all lines in localization file. If syntax invalid, throws {@code LXCSyntaxException}.
     * @since 1.1
     * @throws LXPSyntaxException if lxc syntax is invalid.
     * */
    private void processStrings() throws LXPSyntaxException {

    }

    private void parseModules(int type) {
        if (type == ModuleTypes.DEFAULT) {

        }
    }

    public Object getModule(String name, int type) {
        return type == ModuleTypes.DEFAULT ? defaultModules.get(name) : type == ModuleTypes.ARRAY ? null : type == ModuleTypes.SUBARRAY ? null : null;
    }

    public static class ModuleTypes {
        private ModuleTypes() {}

        public static final int DEFAULT = 0, ARRAY = 1, SUBARRAY = 2;
    }

    /**
     * Set current language to {@code newLanguage}.
     * @param newLanguage new language.
     * @since 1.1
     * */
    public void setLanguage(String newLanguage) {
        language = newLanguage;
    }

    /**
     * Get index of language {@code language} from <em>header</em>. Exist for debugging.
     * @return index of {@code language}.
     * @param language language name from <em>header</em>.
     * @since 1.1
     * @see #languagesIndex
     * */
    public int getLanguageIndex(String language) {
        return languagesIndex.get(language);
    }

    /**
     * Get value of {@code key} from file.
     * @return value of{@code key}.
     * @param key key of translation from file.
     * @since 1.1
     * @see #translations
     * */
    public String get(String key) {
        return "";
    }

    /**
     * Read next line and return it if line doesn't comment or empty and removes all comments.
     * @return valid line
     * @param reader initialized BufferedReader.
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