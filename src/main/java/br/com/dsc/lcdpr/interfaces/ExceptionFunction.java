package br.com.dsc.lcdpr.interfaces;

@FunctionalInterface
public interface ExceptionFunction<T, R> {

    R apply(T t) throws Exception;

}
