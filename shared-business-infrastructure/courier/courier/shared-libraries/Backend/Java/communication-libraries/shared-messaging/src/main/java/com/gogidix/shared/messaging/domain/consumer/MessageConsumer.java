package com.gogidix.shared.messaging.domain.consumer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Interface for consuming messages from messaging systems.
 * Supports various consumption patterns including subscription, polling, and request-reply.
 */
public interface MessageConsumer {

    /**
     * Subscribe to a destination with a simple message handler
     *
     * @param destination   The destination to subscribe to
     * @param messageHandler The handler for received messages
     * @param messageType   The type of message
     * @param <T>           The message type
     * @return A subscription handle
     */
    <T> Subscription subscribe(String destination, Consumer<T> messageHandler, Class<T> messageType);

    /**
     * Subscribe to a destination with consumer options
     *
     * @param options        The consumer options
     * @param messageHandler The handler for received messages
     * @param messageType    The type of message
     * @param <T>            The message type
     * @return A subscription handle
     */
    <T> Subscription subscribe(ConsumerOptions options, Consumer<T> messageHandler, Class<T> messageType);

    /**
     * Subscribe to a destination with a context-aware message handler
     *
     * @param destination   The destination to subscribe to
     * @param messageHandler The handler for received messages with context
     * @param messageType   The type of message
     * @param <T>           The message type
     * @return A subscription handle
     */
    <T> Subscription subscribeWithContext(String destination, MessageHandler<T> messageHandler, Class<T> messageType);

    /**
     * Poll for a single message
     *
     * @param destination The destination to poll from
     * @param messageType The type of message
     * @param timeoutMs   The timeout in milliseconds
     * @param <T>         The message type
     * @return The received message or null if timeout
     */
    <T> ReceivedMessage<T> poll(String destination, Class<T> messageType, long timeoutMs);

    /**
     * Poll for a batch of messages
     *
     * @param destination  The destination to poll from
     * @param messageType  The type of message
     * @param maxMessages  The maximum number of messages
     * @param timeoutMs    The timeout in milliseconds
     * @param <T>          The message type
     * @return List of received messages
     */
    <T> List<ReceivedMessage<T>> pollBatch(String destination, Class<T> messageType, int maxMessages, long timeoutMs);

    /**
     * Receive and reply pattern
     *
     * @param destination   The destination to listen on
     * @param processor     The request processor
     * @param requestType   The request type
     * @param responseType  The response type
     * @param <Req>         The request type
     * @param <Res>         The response type
     * @return A future that completes when the processor is registered
     */
    <Req, Res> CompletableFuture<Void> receiveAndReply(
            String destination,
            RequestProcessor<Req, Res> processor,
            Class<Req> requestType,
            Class<Res> responseType);

    /**
     * Create a temporary queue
     *
     * @return The name of the temporary queue
     */
    String createTemporaryQueue();

    /**
     * Subscription handle
     */
    interface Subscription {
        String getId();
        String getDestination();
        boolean isActive();
        LocalDateTime getStartedAt();
        long getMessagesProcessed();
        long getMessagesErrored();
        void pause();
        void resume();
        void stop();
        CompletableFuture<Void> stopAsync();
        void addStatisticsListener(StatisticsListener listener);
        void removeStatisticsListener(StatisticsListener listener);
    }

    /**
     * Consumer options
     */
    class ConsumerOptions {
        private final String destination;
        private String consumerGroup = "default";
        private int prefetchCount = 10;
        private boolean autoAck = true;
        private String deadLetterQueue;
        private long startPosition = 0; // 0 = latest, -1 = earliest

        private ConsumerOptions(String destination) {
            this.destination = destination;
        }

        public static ConsumerOptions forDestination(String destination) {
            return new ConsumerOptions(destination);
        }

        public ConsumerOptions withGroup(String group) {
            this.consumerGroup = group;
            return this;
        }

        public ConsumerOptions withPrefetch(int count) {
            this.prefetchCount = count;
            return this;
        }

        public ConsumerOptions withAutoAck() {
            this.autoAck = true;
            return this;
        }

        public ConsumerOptions withManualAck() {
            this.autoAck = false;
            return this;
        }

        public ConsumerOptions withDeadLetter(String dlq) {
            this.deadLetterQueue = dlq;
            return this;
        }

        public ConsumerOptions fromLatest() {
            this.startPosition = 0;
            return this;
        }

        public ConsumerOptions fromEarliest() {
            this.startPosition = -1;
            return this;
        }

        public String getDestination() { return destination; }
        public String getConsumerGroup() { return consumerGroup; }
        public int getPrefetchCount() { return prefetchCount; }
        public boolean isAutoAck() { return autoAck; }
        public String getDeadLetterQueue() { return deadLetterQueue; }
        public long getStartPosition() { return startPosition; }
    }

    /**
     * Received message wrapper
     */
    interface ReceivedMessage<T> {
        T getPayload();
        MessageContext getContext();
        String getMessageId();
        Map<String, Object> getHeaders();
        LocalDateTime getReceivedAt();
        void acknowledge();
        void reject();
        void reject(boolean requeue);
    }

    /**
     * Message context
     */
    interface MessageContext {
        String getMessageId();
        String getDestination();
        Map<String, Object> getHeaders();
        Object getHeader(String key);
        Integer getPartition();
        Long getOffset();
        LocalDateTime getReceivedAt();
        String getConsumerGroup();
        int getRetryAttempt();
        void acknowledge();
        void reject();
        void reject(boolean requeue);
        void reply(Object response);
        void replyAndCorrelate(Object response, String correlationId);
        void addProcessingMetadata(String key, Object value);
        Object getProcessingMetadata(String key);
        AutoCloseable createSpan(String operationName);
        void log(String level, String message, Object... args);
    }

    /**
     * Context-aware message handler
     */
    @FunctionalInterface
    interface MessageHandler<T> {
        void handle(T message, MessageContext context);
    }

    /**
     * Request processor for request-reply pattern
     */
    @FunctionalInterface
    interface RequestProcessor<Req, Res> {
        Res process(Req request, MessageContext context);
    }

    /**
     * Statistics listener
     */
    interface StatisticsListener {
        void onMessageProcessed(String subscriptionId, long count);
        void onMessageErrored(String subscriptionId, long count);
        void onLatencyMeasured(String subscriptionId, long latencyMs);
    }
}
