package com.gogidix.aiservices.aifrauddetectionservice.application.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CQRS Command Gateway for dispatching commands to their registered handlers.
 * Acts as a dispatcher that routes commands to appropriate handlers.
 */
@Component
@Slf4j
public class CommandGateway {

    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new ConcurrentHashMap<>();

    /**
     * Registers a command handler for a specific command type.
     *
     * @param commandClass the command class this handler processes
     * @param handler the handler implementation
     * @param <C> the command type
     * @param <R> the result type
     */
    public <C extends Command<R>, R> void registerHandler(Class<C> commandClass, CommandHandler<C, R> handler) {
        handlers.put(commandClass, handler);
        log.debug("Registered handler for command type: {}", commandClass.getSimpleName());
    }

    /**
     * Dispatches a command to its registered handler.
     *
     * @param command the command to dispatch
     * @param <C> the command type
     * @param <R> the result type
     * @return the result of command execution
     * @throws Exception if no handler is registered or execution fails
     */
    @SuppressWarnings("unchecked")
    public <C extends Command<R>, R> R dispatch(C command) throws Exception {
        CommandHandler<C, R> handler = (CommandHandler<C, R>) handlers.get(command.getClass());

        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for command: " + command.getClass().getSimpleName());
        }

        log.debug("Dispatching command: {}", command.getClass().getSimpleName());
        return handler.handle(command);
    }

    /**
     * Checks if a handler is registered for the given command type.
     *
     * @param commandClass the command class to check
     * @return true if a handler is registered, false otherwise
     */
    public boolean hasHandlerFor(Class<?> commandClass) {
        return handlers.containsKey(commandClass);
    }
}
