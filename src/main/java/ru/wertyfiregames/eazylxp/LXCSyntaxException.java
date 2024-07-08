/**
 * File created on 18:57 03.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

import java.io.Serial;

/**
 * Thrown from {@link LXCParser} to indicate that parsing {@code lxc file} has invalid syntax.
 * @since 1.0
 * @author Wertyfire
 * */
public class LXCSyntaxException extends Exception {
    @Serial
    private static final long serialVersionUID = -4249599111868885875L;

    /**
     * Constructs a {@code LXCSyntaxException} with no
     * detail message.
     * @since 1.0
     * */
    public LXCSyntaxException() {
        super();
    }

    /**
     * Constructs a {@code LXCSyntaxException} with the
     * specified detail message.
     * @param s the detail message.
     * @since 1.0
     * */
    public LXCSyntaxException(String s) {
        super(s);
    }
}