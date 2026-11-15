package ru.wertyfiregames.eazylxp;

/**
 * A fallback no-op implementation of {@link ILXParser}.
 * Used by {@link LocalizationFile} when a localization file cannot be found, read, or parsed.
 * <p>Calling {@link #get(String)} always returns the key itself, and {@link #setLanguage(String)} does nothing.
 *
 * @since 1.2
 * @author Wertyfire
 * @see ILXParser
 * @see LocalizationFile
 * */
public class EmptyLXParser implements ILXParser {
    @Override
    public String get(String key) {
        return key;
    }
    @Override
    public void setLanguage(String newLanguage) {
        //do nothing
    }

    /**
     * Constructs a new {@code EmptyLXParser} instance.
     * @since 1.2
     */
    public EmptyLXParser() {}
}