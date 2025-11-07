package com.example.command;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommandRequest(
        CommandType command,
        String repository,
        String branch,
        String version,
        Map<String, String> options) {

    public CommandRequest {
        command = command != null ? command : CommandType.UNKNOWN;
        options = options == null || options.isEmpty() ? null : Map.copyOf(options);
    }

    @JsonCreator
    public CommandRequest(
            @JsonProperty(value = "command", required = true) String command,
            @JsonProperty("repository") String repository,
            @JsonProperty("branch") String branch,
            @JsonProperty("version") String version,
            @JsonProperty("options") Map<String, String> options) {
        this(parseCommand(command), repository, branch, version, options);
    }

    private static CommandType parseCommand(String rawCommand) {
        if (rawCommand == null) {
            return CommandType.UNKNOWN;
        }
        try {
            return CommandType.valueOf(rawCommand.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return CommandType.UNKNOWN;
        }
    }
}
