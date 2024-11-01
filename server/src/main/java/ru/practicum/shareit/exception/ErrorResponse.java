package ru.practicum.shareit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final String error;
    private String stackTrace;

    public ErrorResponse(Throwable exception, boolean showStackTrace) {
        this(exception.getMessage());
        stackTrace = showStackTrace ? getStackTrace(exception) : "hide";
    }

    private static String getStackTrace(Throwable throwable) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }
}
