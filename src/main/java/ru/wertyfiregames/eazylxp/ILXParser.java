/**
 * File created on 16:39 04.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

import java.io.File;
import java.io.IOException;

/**
 * Implementing this interface defines a parser for LX localization formats. It is used by {@link LocalizationFile} to
 * support multiple file extensions {@code .lxc} for basic table-based localization and {@code .lxp} for script-like translations).
 * @since 1.0
 * @author Wertyfire
 * */
public interface ILXParser {
    /**
     * Returns the translated value for the specified {@code key} in the current language.
     * @param key the translation key in the localization file.
     * @return the translated value for {@code key} in the current language (configured via {@link #setLanguage(String)}), or {@code key} itself if not found.
     * @since 1.0
     * */
    String get(String key);

    /**
     * Sets the current active language to {@code newLanguage}.
     * @param newLanguage the language name as defined in the localization file.
     * @since 1.0
     * */
    void setLanguage(String newLanguage);

    /**
     * Sets or updates translations for the specified {@code key} across one or more languages.
     * <p>The {@code values} parameter must contain alternating language names and translated strings
     * (e.g., {@code "english", "Hello World!", "russian", "Привет, мир!"}).
     * @param key the translation key to add or update.
     * @param values alternating pairs of language names and their corresponding translations.
     * @return this parser instance after updating translations.
     * @since 1.3
     * */
    ILXParser set(String key, String... values);

    /**
     * Saves all loaded translations back to the original localization file.
     * @throws IOException if an I/O error occurs while writing to the file.
     * @since 1.3
     * @see #saveTo(File)
     * */
    void save() throws IOException;

    /**
     * Saves all loaded translations to the specified target {@code file} in UTF-8 encoding.
     * @param file the destination file to write the localization data to.
     * @throws IOException if an I/O error occurs while writing to the file.
     * @since 1.3
     * @see #save()
     * */
    void saveTo(File file) throws IOException;
}