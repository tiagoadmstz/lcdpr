package br.com.dsc.lcdpr.util;

import br.com.dsc.lcdpr.interfaces.ExceptionFunction;

public abstract class ExceptionUtil {

    public static void tryCatch(ExceptionFunction exceptionFunction) {
        try {
            exceptionFunction.apply(null);
        } catch (Exception ex) {
        }
    }

    public static <R extends Object> R tryCatch(R defaultReturn, ExceptionFunction exceptionFunction) {
        try {
            return (R) exceptionFunction.apply(defaultReturn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return defaultReturn;
    }

    public static <R extends Object> R tryCatch(Object object, R defaultReturn, ExceptionFunction exceptionFunction) {
        try {
            return (R) exceptionFunction.apply(object);
        } catch (Exception ex) {
        }
        return defaultReturn;
    }

}
