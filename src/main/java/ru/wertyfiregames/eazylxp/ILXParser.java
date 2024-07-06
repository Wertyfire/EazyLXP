/**
 * File created on 16:39 04.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

/**
 * Implementing this interface means creating new parser for LX. This is used in {@link LocalizationFile} to
 * parse multiply extensions (.lxc for default localization and .lxp for script-like translations).
 * @since 1.0
 * @author Wertyfire
 * */
public interface ILXParser {
    /**
     * Method returns value of key in localization file in current language.
     * @return value of {@code key} from localization according to current language setted by {@link #setLanguage(String)}.
     * @param key key of translation in localization file.
     * @since 1.0
     * */
    String get(String key);
    /**
     * Set current language to {@code newLanguage}.
     * @param newLanguage new language.
     * @since 1.0
     * */
    void setLanguage(String newLanguage);
}