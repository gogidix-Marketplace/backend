package com.gogidix.customersupport.phonesupport.interfaces.rest;

import com.gogidix.customersupport.phonesupport.domain.model.CallQueue;
import com.gogidix.customersupport.phonesupport.domain.model.PhoneCall;
import com.gogidix.customersupport.phonesupport.application.service.PhoneSupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/phone-support")
@RequiredArgsConstructor
@Tag(name = "Phone Support", description = "APIs for managing phone calls and call queues")
public class PhoneSupportController {

    private final PhoneSupportService phoneSupportService;

    @GetMapping("/calls")
    @Operation(summary = "Get all calls", description = "Retrieve all phone calls")
    public ResponseEntity<List<PhoneCall>> getAllCalls() {
        return ResponseEntity.ok(phoneSupportService.getAllCalls());
    }

    @GetMapping("/calls/{id}")
    @Operation(summary = "Get call by ID", description = "Retrieve a specific phone call by ID")
    public ResponseEntity<PhoneCall> getCallById(@PathVariable String id) {
        return ResponseEntity.ok(phoneSupportService.getCallById(id));
    }

    @GetMapping("/calls/call-id/{callId}")
    @Operation(summary = "Get call by call ID", description = "Retrieve a phone call by call ID")
    public ResponseEntity<PhoneCall> getCallByCallId(@PathVariable String callId) {
        return ResponseEntity.ok(phoneSupportService.getCallByCallId(callId));
    }

    @GetMapping("/calls/agent/{agentId}")
    @Operation(summary = "Get calls by agent", description = "Retrieve calls handled by an agent")
    public ResponseEntity<List<PhoneCall>> getCallsByAgent(@PathVariable String agentId) {
        return ResponseEntity.ok(phoneSupportService.getCallsByAgent(agentId));
    }

    @GetMapping("/calls/status/{callStatus}")
    @Operation(summary = "Get calls by status", description = "Retrieve calls by status")
    public ResponseEntity<List<PhoneCall>> getCallsByStatus(@PathVariable PhoneCall.CallStatus callStatus) {
        return ResponseEntity.ok(phoneSupportService.getCallsByStatus(callStatus));
    }

    @GetMapping("/calls/daterange")
    @Operation(summary = "Get calls by date range", description = "Retrieve calls within a date range")
    public ResponseEntity<List<PhoneCall>> getCallsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate) {
        return ResponseEntity.ok(phoneSupportService.getCallsByDateRange(startDate, endDate));
    }

    @PostMapping("/calls/initiate")
    @Operation(summary = "Initiate a call", description = "Initiate a new phone call")
    public ResponseEntity<PhoneCall> initiateCall(
            @RequestParam String callId,
            @RequestParam String phoneNumber,
            @RequestParam String callerName,
            @RequestParam PhoneCall.CallDirection direction) {
        PhoneCall.CallerInfo callerInfo = PhoneCall.CallerInfo.builder()
                .phoneNumber(phoneNumber)
                .name(callerName)
                .build();
        PhoneCall call = phoneSupportService.initiateCall(callId, callerInfo, direction);
        return ResponseEntity.status(HttpStatus.CREATED).body(call);
    }

    @PutMapping("/calls/{callId}/answer")
    @Operation(summary = "Answer a call", description = "Answer a phone call")
    public ResponseEntity<PhoneCall> answerCall(
            @PathVariable String callId,
            @RequestParam String agentId,
            @RequestParam String agentName) {
        return ResponseEntity.ok(phoneSupportService.answerCall(callId, agentId, agentName));
    }

    @PutMapping("/calls/{callId}/end")
    @Operation(summary = "End a call", description = "End a phone call")
    public ResponseEntity<PhoneCall> endCall(@PathVariable String callId) {
        return ResponseEntity.ok(phoneSupportService.endCall(callId));
    }

    @PutMapping("/calls/{callId}/notes")
    @Operation(summary = "Update call notes", description = "Update notes for a phone call")
    public ResponseEntity<PhoneCall> updateCallNotes(
            @PathVariable String callId,
            @RequestParam String notes) {
        return ResponseEntity.ok(phoneSupportService.updateCallNotes(callId, notes));
    }

    @PutMapping("/calls/{callId}/link-ticket")
    @Operation(summary = "Link call to ticket", description = "Link a phone call to a support ticket")
    public ResponseEntity<PhoneCall> linkToTicket(
            @PathVariable String callId,
            @RequestParam String ticketId) {
        return ResponseEntity.ok(phoneSupportService.linkToTicket(callId, ticketId));
    }

    @DeleteMapping("/calls/{id}")
    @Operation(summary = "Delete a call", description = "Delete a phone call record")
    public ResponseEntity<Void> deleteCall(@PathVariable String id) {
        phoneSupportService.deleteCall(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/queues")
    @Operation(summary = "Get all queues", description = "Retrieve all call queues")
    public ResponseEntity<List<CallQueue>> getAllQueues() {
        return ResponseEntity.ok(phoneSupportService.getAllQueues());
    }

    @GetMapping("/queues/active")
    @Operation(summary = "Get active queues", description = "Retrieve all active call queues")
    public ResponseEntity<List<CallQueue>> getActiveQueues() {
        return ResponseEntity.ok(phoneSupportService.getActiveQueues());
    }

    @PostMapping("/queues")
    @Operation(summary = "Create a queue", description = "Create a new call queue")
    public ResponseEntity<CallQueue> createQueue(
            @RequestParam String queueName,
            @RequestParam String queueId,
            @RequestParam(required = false) String description) {
        CallQueue queue = phoneSupportService.createQueue(queueName, queueId, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(queue);
    }
}
