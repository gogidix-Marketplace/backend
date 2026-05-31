package com.gogidix.aiservices.aifrauddetectionservice.application.command;

/**
 * Functional interface for handling CQRS commands.
 * Implementations define the business logic for processing specific command types.
 *
 * @param <C> The type of command this handler processes
 * @param <R> The type of result returned from command execution
 */
@FunctionalInterface
public interface CommandHandler<C extends Command<R>, R> {

    /**
     * Handles the given command and returns a result.
     *
     * @param command the command to handle
     * @return the result of command execution
     * @throws Exception if command processing fails
     */
    R handle(C command) throws Exception;
}
