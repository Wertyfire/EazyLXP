/**
 * Contains classes for parsing localization files.
 * <h2>How to use:</h2>
 * <h3>1.1: Localization Extension Container</h3>
 * LXC files can be saved with two extensions: .lxc and .lexc. There is no difference.
 * Example syntax of file:
 * <blockquote><pre>
 * !version 3
 * key|language_one|language_two
 * key_of_translation|Translation in language one|Translation in language two
 * other_key|Other key|Yek rehto
 * </pre></blockquote>
 * On the first line, you set the version of LXC. The version currently supported by this library (1.2) is 3,
 * so you should write '!version 3'.<p>
 * The next line is the header. Here you can specify the languages supported by your program.
 * The line should start with "key" and then you can add new languages by typing '|' followed by the language names.
 * There are no limits or rules for languages - you can write whatever you want,
 * as long as you pass the exact same name to the {@link ru.wertyfiregames.eazylxp.LocalizationFile#setLanguage(java.lang.String) setLanguage} method.
 * On the following lines you can add translations in the format "translation_key|" followeb by the translations
 * matching the <em>header</em>. If any translations are missing, the program will throw an {@link ru.wertyfiregames.eazylxp.LXCSyntaxException LXCSyntaxException}.
 * You can add one-line comments by writing '#'. All text after '#' is treated as a comment, and commenting cannot be canceled.
 * <h3>1.2: Parsing code and getting it from file:</h3>
 * You can write your own reader or use {@linkplain LocalizationFile}, which suppresses all exceptions.
 * <blockquote><pre>{@code
 * package default;
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
 * */
package ru.wertyfiregames.eazylxp;