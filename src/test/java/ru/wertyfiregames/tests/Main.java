/**
 * File created on 17:26 03.07.2024 by Wertyfire
 */

package ru.wertyfiregames.tests;

import ru.wertyfiregames.eazylxp.LocalizationFile;

import java.io.File;

public class Main {
    static void main(String... args) {
        LocalizationFile lxc = new LocalizationFile("/default.lexc", "russian");
        LocalizationFile lxcMerge = new LocalizationFile("/merge_translations.lxc", "english");
        lxc = lxc.merge(lxcMerge, true);
        lxc.setLanguage("english");
        System.out.println(lxc.get("hello_world"));
        System.out.println(lxc.get("escape_test"));

        LocalizationFile toSet = lxcMerge;
        toSet.set("hello_world", "english", "Hello, World!", "russian", "Привет, мир!", "uzbek", "Я люблю шаурму")
                .set("sus", "english", "Sussy Baka");
        toSet.saveTo(new File("src\\test\\resources\\new_translations.lexc"));
        toSet.setLanguage("third");
        System.out.println(toSet.get("sus"));
    }
}