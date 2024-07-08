/**
 * File created on 20:47 07.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

import java.io.Serial;

/**
 * Thrown from {@link LXPParser} to indicate that parsing {@code lxp file} has invalid syntax.
 * @since 1.1
 * @author Wertyfire
 * */
public class LXPSyntaxException extends Exception {
    @Serial
    private final static long serialVersionUID = 690192908554037111L;

    /**
     * Constructs a {@code LXPSyntaxException} with no
     * detail message.
     * @since 1.1
     * */
    public LXPSyntaxException() {
        super();
    }

    /**
     * Constructs a {@code LXCSyntaxException} with the
     * specified detail message.
     * @param s the detail message.
     * @since 1.1
     * */
    public LXPSyntaxException(String s) {
        super(s);
    }
}