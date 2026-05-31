package com.gogidix.sales.communication.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Message Queries (Input Port)
 * Defines the input queries for message operations
 */
public interface MessageQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetMessageByIdQuery {
        private String messageId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetMessagesByConversationQuery {
        private String conversationId;
        private int page;
        private int size;
        private String sortBy;
        private String sortDirection;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetMessagesBySenderQuery {
        private String senderId;
        private int page;
        private int size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetUnreadMessagesQuery {
        private String userId;
        private int page;
        private int size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchMessagesQuery {
        private String searchTerm;
        private Instant startDate;
        private Instant endDate;
        private int page;
        private int size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetMessagesByStatusQuery {
        private String status;
        private int page;
        private int size;
    }
}
