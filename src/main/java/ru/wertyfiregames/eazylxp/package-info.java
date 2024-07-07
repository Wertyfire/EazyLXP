/**
 * Package contains classes for parsing localization files.
 * <h2>How to use:</h2>
 * <h3>1.1: Localization Extension Container</h3>
 * LXC Can be in 2 extensions: .lxc and .lexc. There is no difference.
 * Example syntax of file:
 * <blockquote><pre>
 * !version 2
 * key|language_one|language_two
 * key_of_translation|Translation on language one|Translation on language two
 * other_key|Other key|Yek rehto
 * </pre></blockquote>
 * On first line we settings version of lxc. Current supported by this version of library (1.0) is 2,
 * so we should write '!version 2'.<p>
 * Next line - header. Here you can specify languages that supported by your program.
 * Line should start with "key|" and then you can add new languages by adding '|' and
 * then writing language names.
 * There is no limits for languages and no rules - you can write what you want,
 * but next you should call method {@link ru.wertyfiregames.eazylxp.LocalizationFile#setLanguage(java.lang.String) setLanguage}.
 * On next lines you can add translations in format "translation_key|" and then write translations
 * according to <em>header</em>. If not all translations added, program will throw {@link ru.wertyfiregames.eazylxp.LXCSyntaxException new LXCSyntaxException()}.
 * You can add one-line comments by writing '#' but comment lines supported <em>only</em> after header on the next line. All text after '#' will be commented. You can't cancel commenting.
 * <h3>1.2: Parsing code and getting it from file:</h3>
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
 * *note1: here you can write new File("path/to/your/file.lxc") or write "path/to/your/file.lxc".
 * <p>If path starts from '/' program will search file by using getClass().getResource(path).
 * */
package ru.wertyfiregames.eazylxp;