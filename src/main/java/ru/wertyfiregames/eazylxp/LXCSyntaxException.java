/**
 * File created on 18:57 03.07.2024 by Wertyfire
 */

package ru.wertyfiregames.eazylxp;

import java.io.Serial;

/**
 * Thrown by {@link LXCParser} to indicate that a parsed {@code .lxc} or {@code .lexc} file contains invalid syntax.
 * @since 1.0
 * @author Wertyfire
 * */
public class LXCSyntaxException extends Exception {
    @Serial
    private static final long serialVersionUID = -4249599111868885875L;

    /**
     * Constructs an {@code LXCSyntaxException} with no detail message.
     *
     * @since 1.0
     */
    public LXCSyntaxException() {
        super();
    }

    /**
     * Constructs an {@code LXCSyntaxException} with the specified detail
     * message.
     * A detail message is a {@code String} that describes this particular
     * exception.
     *
     * @param msg the detail message.
     * @since 1.0
     */
    public LXCSyntaxException(String msg) {
        super(msg);
    }

    /**
     * Creates an {@code LXCSyntaxException} with the specified
     * detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.2
     */
    public LXCSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an {@code LXCSyntaxException} with the specified cause
     * and a detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of
     * {@code cause}).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.2
     */
    public LXCSyntaxException(Throwable cause) {
        super(cause);
    }
}