/**
 * File created on 17:08 03.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * High-level wrapper for {@code .lxc} and {@code .lxp} localization files.
 * Delegates parsing to {@link LXCParser} and {@link LXPParser} respectively while suppressing exceptions.
 * If loading or parsing fails, the error is logged and {@link EmptyLXParser} is used as a fallback.
 * @since 1.0
 * @author Wertyfire
 * @see ILXParser
 * @see LXCParser
 * @see LXPParser
 * @see EmptyLXParser
 * */
public class LocalizationFile {
    /**
     * The underlying parser instance used to delegate {@link #setLanguage(String)} and {@link #get(String)} calls.
     * @since 1.0
     * */
    private ILXParser lxParser = new EmptyLXParser();

    /**
     * Logger used to report file loading or syntax parsing errors.
     * @since 1.2
     * */
    private static final Logger LOGGER = Logger.getLogger(LocalizationFile.class.getName());

    /**
     * Creates a new {@code LocalizationFile} from the given file path or classpath resource path.
     * Automatically selects the appropriate parser based on the file extension ({@code .lxc}, {@code .lexc}, {@code .lxp}, or {@code .lexp}).
     * <p>If the path starts with {@code '/'}, the file is loaded from classpath resources.
     * If loading or parsing fails, the error is logged and {@link EmptyLXParser} is used as a fallback.
     * @param lxFilepath the path to the localization file, or a classpath resource path starting with {@code '/'}.
     * @param defaultLanguage the initial active language as defined in the file header.
     * @since 1.0
     * @see LocalizationFile#LocalizationFile(File, String)
     * */
    public LocalizationFile(String lxFilepath, String defaultLanguage) {
        try {
            File file = lxFilepath.startsWith("/") ?
                    new File(Objects.requireNonNull(getClass().getResource(lxFilepath)).toURI()) :
                    new File(lxFilepath);
            initParser(file, defaultLanguage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load localization file: " + lxFilepath, e);
        }
    }

    /**
     * Creates a new {@code LocalizationFile} from the specified {@link File} object.
     * Same as {@link #LocalizationFile(String, String)}, but accepts a {@link File} instead of a path string.
     * Automatically selects the appropriate parser based on the file extension ({@code .lxc}, {@code .lexc}, {@code .lxp}, or {@code .lexp}).
     * <p>If loading or parsing fails, the error is logged and {@link EmptyLXParser} is used as a fallback.
     * @param lxFile the localization {@link File} to parse.
     * @param defaultLanguage the initial active language as defined in the file header.
     * @since 1.1
     * @see #LocalizationFile(String, String)
     * */
    public LocalizationFile(File lxFile, String defaultLanguage) {
        try {
            initParser(lxFile, defaultLanguage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load localization file: " + (lxFile != null ? lxFile.getPath() : "null"), e);
        }
    }

    /**
     * Creates a new {@code LocalizationFile} wrapping an existing {@link ILXParser} instance.
     * If {@code fromParser} is {@code null}, {@link EmptyLXParser} is used as a fallback.
     * @param fromParser the underlying {@link ILXParser} implementation to delegate calls to.
     * @since 1.3
     * */
    public LocalizationFile(ILXParser fromParser) {
        lxParser = fromParser != null ? fromParser : new EmptyLXParser();
    }

    /**
     * Initializes the appropriate {@link ILXParser} implementation based on the file extension.
     * @param lxFile the localization file to parse.
     * @param defaultLanguage the initial active language.
     * @throws IOException if an I/O error occurs while reading the file.
     * @throws LXCSyntaxException if the file contains invalid LXC syntax
     * @since 1.2
     * */
    private void initParser(File lxFile, String defaultLanguage) throws IOException, LXCSyntaxException {
        if (lxFile.getAbsolutePath().endsWith(".lxc") || lxFile.getAbsolutePath().endsWith(".lexc")) {
            lxParser = new LXCParser(lxFile, defaultLanguage);
        } else if (lxFile.getAbsolutePath().endsWith(".lxp") || lxFile.getAbsolutePath().endsWith(".lexp")) {
            lxParser = new LXPParser(lxFile, defaultLanguage);
        }
    }

    /**
     * Sets the current active language for translations.
     * Delegates the call to the underlying {@link ILXParser} implementation.
     * @param language the language name as defined in the localization file header.
     * @since 1.0
     * @see ILXParser#setLanguage(String)
     * */
    public void setLanguage(String language) {
        lxParser.setLanguage(language);
    }

    /**
     * Retrieves the translated string for the specified {@code key} in the current language.
     * Delegates the call to the underlying {@link ILXParser} implementation.
     *
     * @param key the translation key in the localization file.
     * @return the translated string for {@code key}, or {@code key} itself if not found or if parsing fails.
     * @since 1.0
     * @see ILXParser#get(String)
     * */
    public String get(String key) {
        return lxParser.get(key);
    }

    /**
     * Sets or updates translations for the specified {@code key} across one or more languages.
     * <p>The {@code values} array must contain alternating language names and translated strings
     * (e.g., {@code "english", "Hello World!", "russian", "Привет, мир!"}). If an error occurs
     * (such as an odd number of elements or {@code null} arguments), it is logged and ignored.
     * @param key the translation key to add or update.
     * @param values alternating pairs of language names and their corresponding translations.
     * @return this {@code LocalizationFile} instance after updating translations.
     * @since 1.3
     * @see ILXParser#set(String, String[])
     * */
    public LocalizationFile set(String key, String... values) {
        try {
            lxParser.set(key, values);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set translation for key: " + key, e);
        }
        return this;
    }

    /**
     * Saves all loaded translations back to the original localization file.
     * <p>If an error occurs while writing to the file, it is logged and ignored.
     * @since 1.3
     * @see #saveTo(File)
     * @see ILXParser#save()
     * */
    public void save() {
        try {
            lxParser.save();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save localization file", e);
        }
    }

    /**
     * Saves all loaded translations to the specified target {@code file}.
     * <p>If an error occurs while writing to the file, it is logged and ignored.
     * @param file the destination file to write the localization data to.
     * @since 1.3
     * @see #save()
     * @see ILXParser#saveTo(File)
     * */
    public void saveTo(File file) {
        try {
            lxParser.saveTo(file);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save localization file: " + (file != null ? file.getPath() : "null"), e);
        }
    }

    /**
     * Merges translations and languages from another {@code LocalizationFile} into this instance,
     * overwriting existing translations when keys and languages collide.
     * <p>This is a convenience overload for {@link #merge(LocalizationFile, boolean)} with
     * {@code prioritizeNew} set to {@code true}.
     * @param localization the {@code LocalizationFile} whose translations will be merged into this instance.
     * @return this {@code LocalizationFile} instance after merging.
     * @since 1.3
     * @see #merge(LocalizationFile, boolean)
     * */
    public LocalizationFile merge(LocalizationFile localization) {
        return merge(localization, true);
    }

    /**
     * Merges translations and languages from another {@code LocalizationFile} into this instance.
     * <p>If {@code localization} is {@code null}, refers to this instance, or uses an incompatible parser,
     * no changes are made.
     * @param localization the {@code LocalizationFile} whose translations will be merged into this instance.
     * @param prioritizeNew {@code true} to overwrite existing translations when keys and languages
     *                                  collide; {@code false} to preserve existing translations and
     *                                  only add missing keys or languages.
     * @return this {@code LocalizationFile} instance after merging.
     * @since 1.3
     * @see LXCParser#merge(LXCParser, boolean)
     * */
    public LocalizationFile merge(LocalizationFile localization, boolean prioritizeNew) {
        if (localization != null && lxParser instanceof LXCParser currentParser && localization.lxParser instanceof LXCParser newParser) {
            currentParser.merge(newParser, prioritizeNew);
        }
        return this;
    }

    /**
     * Returns the underlying {@link ILXParser} instance used by this wrapper.
     * <p>If initialization fails, this method returns the fallback {@link EmptyLXParser}.
     * @return the underlying {@link ILXParser} implementation.
     * @since 1.3
     * @see ILXParser
     * @see EmptyLXParser
     * */
    public ILXParser direct() {
        return lxParser;
    }
}