package org.budgiant.common.exception;

/**
 * @author vvk
 * @since 2019/9/29
 */
public class ValidateException extends RuntimeException {

    public ValidateException() {
        super();
    }

    public ValidateException(String message) {
        super(message);
    }
}
