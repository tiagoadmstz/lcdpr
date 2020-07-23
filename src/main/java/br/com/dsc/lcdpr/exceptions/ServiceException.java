package br.com.dsc.lcdpr.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceException extends RuntimeException {

    private List<String> messages = new ArrayList();

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Object... args) {
        super(buildMessage(message, args));
    }

    public ServiceException(List<String> messages) {
        super(messages.stream().collect(Collectors.joining(",\n")));
    }

    public static String buildMessage(String message, Object... args) {
        for (Object arg : args) message = message.replace("{}", arg.toString());
        return message;
    }

}
