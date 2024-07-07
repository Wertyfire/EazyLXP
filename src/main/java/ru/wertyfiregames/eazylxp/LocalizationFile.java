/**
 * File created on 17:08 03.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Class to get translations from lxc and lxp files via LXCParser and LXPParser respectively.
 * @since 1.0
 * @author Wertyfire
 * @see ILXParser
 * @see LXCParser
 * @see LXPParser
 * */
public class LocalizationFile {
    /**
     * ILXParser instead of LXCParser or LXPParser because of methods
     * {@code setLanguage} and {@code get} here.
     * @since 1.0
     * */
    private ILXParser lxParser;

    public LocalizationFile(String lxFilepath, String defaultLanguage) {
        if (lxFilepath.endsWith(".lxc") || lxFilepath.endsWith(".lexc")) {
            if (!lxFilepath.startsWith("/"))
                lxParser = new LXCParser(new File(lxFilepath), defaultLanguage);
            else {
                try {
                    lxParser = new LXCParser(new File(Objects.requireNonNull(getClass().getResource(lxFilepath)).toURI()), defaultLanguage);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Call method {@linkplain ILXParser#setLanguage(String)}.
     * ILXParser implemented by parser initialized in constructor.
     * @since 1.0
     * @see ILXParser#setLanguage(String)
     * @see LXCParser#setLanguage(String)
     * @see LXPParser#setLanguage(String)
     * */
    public void setLanguage(String language) {
        lxParser.setLanguage(language);
    }

    /**
     * Call method {@linkplain ILXParser#get(String)}.
     * ILXParser implemented by parser initialized in constructor.
     * @since 1.0
     * @see ILXParser#get(String)
     * @see LXCParser#get(String)
     * @see LXPParser#get(String)
     * */
    public String get(String key) {
        return lxParser.get(key);
    }
}