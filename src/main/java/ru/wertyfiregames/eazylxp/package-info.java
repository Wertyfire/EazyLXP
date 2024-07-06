/**
 * Package contains classes for parsing localization files.
 * <h3>How to use:</h3>
 * <p>
 * <h4>1.1: Localization Extension Container</h4><p>
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
 * You can add one-line comments by writing '#'. All text after '#' will be commented. You can't cancel commenting.
 * <h4>2.1: Parsing code and getting it from file:</h4>
 * <blockquote><pre>
 * package default;
 *
 * import {@linkplain ru.wertyfiregames.eazylxp.LocalizationFile ru.wertyfiregames.eazylxp.LocalizationFile};
 *
 * public class Main {
 *     public static void main(String[] args) {
 *         {@linkplain ru.wertyfiregames.eazylxp.LocalizationFile LocalizationFile} lxc = {@linkplain ru.wertyfiregames.eazylxp.LocalizationFile#LocalizationFile(java.lang.String, java.lang.String) new LocalizationFile(*note1, "any_lang_from_header")};
 *         {@linkplain ru.wertyfiregames.eazylxp.LocalizationFile#setLanguage(java.lang.String) lxc.setLanguage("language_two");}
 *         System.out.println({@linkplain ru.wertyfiregames.eazylxp.LocalizationFile#get(java.lang.String) lxc.get("other_key")});
 *     }
 * }
 * </pre></blockquote>
 * Console output: "Yek rehto".
 * <p>
 * *note1: here you can write new File("path/to/your/file.lxc") or write "path/to/your/file.lxc".
 * <p>If path starts from '/' program will search file by using getClass().getResource(path).
 * */
package ru.wertyfiregames.eazylxp;