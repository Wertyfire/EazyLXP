/**
 * File created on 16:39 04.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

/**
 * Implementing this interface defines a parser for LX localization formats. It is used by {@link LocalizationFile} to
 * support multiple file extensions (@{code .lxc} for basic table-based localization and {@code .lxp} for script-like translations).
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
}