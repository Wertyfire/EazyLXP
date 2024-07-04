/**
 * File created on 17:08 03.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

public class LocalizationFile {
    private ILXParser lxParser;

    public LocalizationFile(String lxFilepath, String defaultLanguage) {
        if (lxFilepath.endsWith(".lxc")) {
            if (lxFilepath.startsWith("/"))
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

    public void setLanguage(String language) {
        lxParser.setLanguage(language);
    }

    public String get(String key) {
        return lxParser.get(key);
    }
}