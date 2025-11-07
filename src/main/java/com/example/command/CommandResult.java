package com.example.command;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommandResult(String status, String message, Object payload) {
    public static CommandResult ok(String message) {
        return new CommandResult("SUCCESS", message, null);
    }

    public static CommandResult error(String message) {
        return new CommandResult("ERROR", message, null);
    }

    public CommandResult withPayload(Object payload) {
        return new CommandResult(status, message, payload);
    }
}
