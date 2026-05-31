package com.gogidix.customersupport.livechat.interfaces.rest;

import com.gogidix.customersupport.livechat.application.dto.ChatMessageRequestDto;
import com.gogidix.customersupport.livechat.application.dto.ChatMessageResponseDto;
import com.gogidix.customersupport.livechat.application.dto.ChatSessionRequestDto;
import com.gogidix.customersupport.livechat.application.dto.ChatSessionResponseDto;
import com.gogidix.customersupport.livechat.application.service.LiveChatService;
import com.gogidix.customersupport.livechat.domain.model.ChatMessage;
import com.gogidix.customersupport.livechat.domain.model.ChatSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/live-chat")
@RequiredArgsConstructor
@Tag(name = "Live Chat", description = "APIs for managing live chat sessions and messages")
public class LiveChatController {

    private final LiveChatService liveChatService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/sessions")
    @Operation(summary = "Get all chat sessions", description = "Retrieve all chat sessions")
    public ResponseEntity<List<ChatSessionResponseDto>> getAllSessions() {
        return ResponseEntity.ok(liveChatService.getAllSessions());
    }

    @GetMapping("/sessions/{id}")
    @Operation(summary = "Get session by ID", description = "Retrieve a specific chat session by ID")
    public ResponseEntity<ChatSessionResponseDto> getSessionById(
            @Parameter(description = "Session ID") @PathVariable String id) {
        return ResponseEntity.ok(liveChatService.getSessionById(id));
    }

    @GetMapping("/sessions/session-id/{sessionId}")
    @Operation(summary = "Get session by session ID", description = "Retrieve a chat session by its unique session ID")
    public ResponseEntity<ChatSessionResponseDto> getSessionBySessionId(
            @Parameter(description = "Session ID") @PathVariable String sessionId) {
        return ResponseEntity.ok(liveChatService.getSessionBySessionId(sessionId));
    }

    @GetMapping("/sessions/customer/{customerId}")
    @Operation(summary = "Get sessions by customer", description = "Retrieve all chat sessions for a customer")
    public ResponseEntity<List<ChatSessionResponseDto>> getSessionsByCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        return ResponseEntity.ok(liveChatService.getSessionsByCustomerId(customerId));
    }

    @GetMapping("/sessions/agent/{agentId}")
    @Operation(summary = "Get sessions by agent", description = "Retrieve all chat sessions assigned to an agent")
    public ResponseEntity<List<ChatSessionResponseDto>> getSessionsByAgent(
            @Parameter(description = "Agent ID") @PathVariable String agentId) {
        return ResponseEntity.ok(liveChatService.getSessionsByAgent(agentId));
    }

    @GetMapping("/sessions/status/{status}")
    @Operation(summary = "Get sessions by status", description = "Retrieve chat sessions filtered by status")
    public ResponseEntity<List<ChatSessionResponseDto>> getSessionsByStatus(
            @Parameter(description = "Chat status") @PathVariable ChatSession.ChatStatus status) {
        return ResponseEntity.ok(liveChatService.getSessionsByStatus(status));
    }

    @GetMapping("/sessions/agent/{agentId}/active")
    @Operation(summary = "Get active sessions by agent", description = "Retrieve active chat sessions for an agent")
    public ResponseEntity<List<ChatSessionResponseDto>> getActiveSessionsByAgent(
            @Parameter(description = "Agent ID") @PathVariable String agentId) {
        return ResponseEntity.ok(liveChatService.getActiveSessionsByAgent(agentId));
    }

    @PostMapping("/sessions")
    @Operation(summary = "Create a chat session", description = "Create a new live chat session")
    public ResponseEntity<ChatSessionResponseDto> createSession(
            @Valid @RequestBody ChatSessionRequestDto request) {
        ChatSessionResponseDto created = liveChatService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/sessions/{id}/assign")
    @Operation(summary = "Assign session to agent", description = "Assign a chat session to an agent")
    public ResponseEntity<ChatSessionResponseDto> assignSession(
            @Parameter(description = "Session ID") @PathVariable String id,
            @Parameter(description = "Agent ID") @RequestParam String agentId,
            @Parameter(description = "Agent name") @RequestParam String agentName) {
        return ResponseEntity.ok(liveChatService.assignSession(id, agentId, agentName));
    }

    @PutMapping("/sessions/{id}/end")
    @Operation(summary = "End a chat session", description = "End an active chat session")
    public ResponseEntity<ChatSessionResponseDto> endSession(
            @Parameter(description = "Session ID") @PathVariable String id) {
        return ResponseEntity.ok(liveChatService.endSession(id));
    }

    @PutMapping("/sessions/{id}/rate")
    @Operation(summary = "Rate a chat session", description = "Submit rating and feedback for a chat session")
    public ResponseEntity<ChatSessionResponseDto> rateSession(
            @Parameter(description = "Session ID") @PathVariable String id,
            @Parameter(description = "Rating (1-5)") @RequestParam Integer rating,
            @Parameter(description = "Feedback") @RequestParam(required = false) String feedback) {
        return ResponseEntity.ok(liveChatService.rateSession(id, rating, feedback));
    }

    @GetMapping("/sessions/{sessionId}/messages")
    @Operation(summary = "Get session messages", description = "Retrieve all messages for a chat session")
    public ResponseEntity<List<ChatMessageResponseDto>> getMessagesBySessionId(
            @Parameter(description = "Session ID") @PathVariable String sessionId) {
        return ResponseEntity.ok(liveChatService.getMessagesBySessionId(sessionId));
    }

    @PostMapping("/messages")
    @Operation(summary = "Send a message", description = "Send a message in a chat session")
    public ResponseEntity<ChatMessageResponseDto> sendMessage(
            @Valid @RequestBody ChatMessageRequestDto request) {
        ChatMessageResponseDto message = liveChatService.sendMessage(request);

        messagingTemplate.convertAndSend("/topic/chat/" + request.getSessionId(), message);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PutMapping("/messages/{messageId}/read")
    @Operation(summary = "Mark message as read", description = "Mark a message as read")
    public ResponseEntity<ChatMessageResponseDto> markMessageAsRead(
            @Parameter(description = "Message ID") @PathVariable String messageId) {
        return ResponseEntity.ok(liveChatService.markMessageAsRead(messageId));
    }

    @DeleteMapping("/messages/{messageId}")
    @Operation(summary = "Delete a message", description = "Delete a chat message")
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "Message ID") @PathVariable String messageId) {
        liveChatService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/count/status/{status}")
    @Operation(summary = "Count sessions by status", description = "Get the count of sessions by status")
    public ResponseEntity<Long> getSessionCountByStatus(
            @Parameter(description = "Chat status") @PathVariable ChatSession.ChatStatus status) {
        return ResponseEntity.ok(liveChatService.getSessionCountByStatus(status));
    }

    @GetMapping("/stats/count/agent/{agentId}/active")
    @Operation(summary = "Count active sessions by agent", description = "Get the count of active sessions for an agent")
    public ResponseEntity<Long> getActiveSessionCountByAgent(
            @Parameter(description = "Agent ID") @PathVariable String agentId) {
        return ResponseEntity.ok(liveChatService.getActiveSessionCountByAgent(agentId));
    }
}
