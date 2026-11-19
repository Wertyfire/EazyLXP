/**
 * Contains classes for parsing localization files.
 * <h2>How to use:</h2>
 * <h3>1.1: Localization Extension Container</h3>
 * LXC files can be saved with two extensions: .lxc and .lexc. There is no difference.
 * Example syntax of file:
 * <blockquote><pre>
 * !version 4
 * key|language_one|language_two
 *
 * #Comment
 * key_of_translation|Translation in language one|Translation in language two
 * other_key|Other key|Yek rehto#Another comment
 * third_key|Translation 1|Translation 2 that includes \| symbol /*this text will
 * be ignored{@code *}/
 * </pre></blockquote>
 * On the first line, you set the version of LXC. The version currently supported by this library (1.3) is 4,
 * so you should write '!version 4'.<p>
 * The next line is the header. Here you can specify the languages supported by your program.
 * The line should start with "key" and then you can add new languages by typing '|' followed by the language names.
 * There are no limits or rules for languages - you can write whatever you want,
 * as long as you pass the exact same name to the {@link LocalizationFile#setLanguage(java.lang.String) setLanguage} method.
 * <p>On the following lines you can add translations in the format "translation_key|" followed by the translations
 * matching the <em>header</em>. If any translations are missing, the program will throw an {@link LXCSyntaxException LXCSyntaxException}.<p>
 * You can add single-line comments with {@code #} and multi-line comments with {@code /* ... *}{@code /}.
 * To include special characters ({@code |}, {@code #}, {@code /*}, or {@code \}) as literal text in translations,
 * escape them with a backslash ({@code \|}, {@code \#}, {@code \/*}, {@code \\}).
 * <h3>1.2: Parsing code and getting it from file:</h3>
 * You can write your own reader or use {@linkplain LocalizationFile}, which suppresses all exceptions.
 * <blockquote><pre>{@code
 * package com.example;
 *
 * import ru.wertyfiregames.eazylxp.LocalizationFile;
 *
 * public class Main {
 *     public static void main(String[] args) {
 *         LocalizationFile lxc = new LocalizationFile(*note1, "any_lang_from_header");
 *         lxc.setLanguage("language_two");
 *         System.out.println(lxc.get("other_key"));
 *     }
 * }
 * }</pre></blockquote>
 * Console output: "Yek rehto".
 * <p>
 * *note1: Here you can pass either new File("path/to/your/file.lxc") or write "path/to/your/file.lxc".
 * <p>If the path starts with '/', the program will search for the file using {@code getClass().getResource(path)}.
 * <h3>1.3: Merging, modifying, and saving translations:</h3>
 * You can combine multiple localization files or add new languages at runtime using
 * {@link LocalizationFile#merge(LocalizationFile)}.
 * <p>To create or update translations programmatically and write changes back to disk, use
 * {@link LocalizationFile#set(String, String...)} and {@link LocalizationFile#save()}
 * or {@link LocalizationFile#saveTo(java.io.File)}.
 * */
package ru.wertyfiregames.eazylxp;