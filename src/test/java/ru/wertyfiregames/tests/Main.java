/**
 * File created on 17:26 03.07.2024 by Wertyfire
 */

package ru.wertyfiregames.tests;

import ru.wertyfiregames.eazylxp.LocalizationFile;

public class Main {
    static void main(String... args) {
        LocalizationFile lxc = new LocalizationFile("/default.lexc", "russian");
        LocalizationFile lxcMerge = new LocalizationFile("/merge_translations.lxc", "english");
        lxc = lxc.merge(lxcMerge, true);
        lxc.setLanguage("english");
        System.out.println(lxc.get("hello_world"));
    }
}