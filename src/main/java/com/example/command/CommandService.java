package com.example.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommandService {

    public CommandResult execute(CommandRequest request) {
        Objects.requireNonNull(request, "request");

        return switch (request.command()) {
            case CLONE_REPOSITORY -> handleClone(request);
            case CREATE_BRANCH -> handleCreateBranch(request);
            case BUMP_VERSION -> handleBumpVersion(request);
            case UNKNOWN -> CommandResult.error("Unsupported command supplied");
        };
    }

    private CommandResult handleClone(CommandRequest request) {
        if (request.repository() == null || request.repository().isBlank()) {
            return CommandResult.error("Repository URL is required for cloning");
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("repository", request.repository());
        payload.put("options", request.options());
        return CommandResult.ok("Clone scheduled").withPayload(payload);
    }

    private CommandResult handleCreateBranch(CommandRequest request) {
        if (request.repository() == null || request.repository().isBlank()) {
            return CommandResult.error("Repository URL is required for branching");
        }
        if (request.branch() == null || request.branch().isBlank()) {
            return CommandResult.error("Branch name is required");
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("repository", request.repository());
        payload.put("branch", request.branch());
        return CommandResult.ok("Branch creation scheduled").withPayload(payload);
    }

    private CommandResult handleBumpVersion(CommandRequest request) {
        if (request.repository() == null || request.repository().isBlank()) {
            return CommandResult.error("Repository URL is required for version bumps");
        }
        if (request.version() == null || request.version().isBlank()) {
            return CommandResult.error("Version is required");
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("repository", request.repository());
        payload.put("version", request.version());
        return CommandResult.ok("Version bump scheduled").withPayload(payload);
    }
}
