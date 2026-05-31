package com.gogidix.customersupport.ticketmanagement.application.mapper;

import com.gogidix.customersupport.ticketmanagement.application.dto.TicketRequestDto;
import com.gogidix.customersupport.ticketmanagement.application.dto.TicketResponseDto;
import com.gogidix.customersupport.ticketmanagement.application.dto.TicketUpdateRequestDto;
import com.gogidix.customersupport.ticketmanagement.domain.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TicketMapperTest {

    private TicketMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TicketMapper();
    }

    @Test
    void toEntity_mapsDtoToTicket() {
        TicketRequestDto dto = TicketRequestDto.builder()
                .title("Test Ticket")
                .description("Desc")
                .priority(TicketRequestDto.TicketPriorityDto.HIGH)
                .customerId("c1")
                .customerEmail("c@e.com")
                .channel(TicketRequestDto.TicketChannelDto.WEB_PORTAL)
                .category("cat1")
                .build();
        Ticket t = mapper.toEntity(dto, "t1");
        assertNotNull(t);
        assertEquals("Test Ticket", t.getTitle());
        assertEquals("Desc", t.getDescription());
        assertEquals("t1", t.getTenantId());
    }

    @Test
    void toResponseDto_mapsEntityToDto() {
        Ticket entity = Ticket.create("t1", "Title", "Desc",
                Ticket.TicketPriority.HIGH, "c1", "c@e.com",
                Ticket.TicketChannel.WEB_PORTAL, "cat1");
        TicketResponseDto dto = mapper.toResponseDto(entity);
        assertNotNull(dto);
        assertEquals("Title", dto.getTitle());
        assertEquals("Desc", dto.getDescription());
        assertNotNull(dto.getStatus());
        assertNotNull(dto.getPriority());
    }

    @Test
    void toResponseDto_withAttachments() {
        Ticket entity = Ticket.create("t1", "Title", "Desc",
                Ticket.TicketPriority.HIGH, "c1", "c@e.com",
                Ticket.TicketChannel.WEB_PORTAL, "cat1");
        Ticket.Attachment att = Ticket.Attachment.builder()
                .fileName("f.txt").fileUrl("http://x").fileSize("100")
                .contentType("text/plain").uploadedAt(Instant.now()).uploadedBy("u1")
                .build();
        entity.setAttachments(List.of(att));
        TicketResponseDto dto = mapper.toResponseDto(entity);
        assertNotNull(dto);
        assertNotNull(dto.getAttachments());
        assertEquals(1, dto.getAttachments().size());
        assertEquals("f.txt", dto.getAttachments().get(0).getFileName());
    }

    @Test
    void toResponseDto_nullAttachments() {
        Ticket entity = Ticket.create("t1", "Title", "Desc",
                Ticket.TicketPriority.HIGH, "c1", "c@e.com",
                Ticket.TicketChannel.WEB_PORTAL, "cat1");
        entity.setAttachments(null);
        TicketResponseDto dto = mapper.toResponseDto(entity);
        assertNotNull(dto);
        assertNull(dto.getAttachments());
    }

    @Test
    void updateEntityFromDto_updatesNonNullFields() {
        Ticket entity = Ticket.create("t1", "Title", "Desc",
                Ticket.TicketPriority.HIGH, "c1", "c@e.com",
                Ticket.TicketChannel.WEB_PORTAL, "cat1");
        TicketUpdateRequestDto dto = TicketUpdateRequestDto.builder()
                .title("New Title")
                .description("New Desc")
                .priority(TicketUpdateRequestDto.TicketPriorityDto.LOW)
                .category("cat2")
                .subCategory("sub1")
                .tags(List.of("tag1"))
                .customerName("Name")
                .customerEmail("new@e.com")
                .customerPhone("123")
                .escalationReason("reason")
                .resolutionNotes("notes")
                .build();
        mapper.updateEntityFromDto(dto, entity);
        assertEquals("New Title", entity.getTitle());
        assertEquals("New Desc", entity.getDescription());
        assertEquals("cat2", entity.getCategory());
        assertEquals("sub1", entity.getSubCategory());
        assertEquals("Name", entity.getCustomerName());
        assertEquals("new@e.com", entity.getCustomerEmail());
        assertEquals("123", entity.getCustomerPhone());
        assertEquals("reason", entity.getEscalationReason());
        assertEquals("notes", entity.getResolutionNotes());
    }

    @Test
    void updateEntityFromDto_nullFields_noUpdate() {
        Ticket entity = Ticket.create("t1", "Title", "Desc",
                Ticket.TicketPriority.HIGH, "c1", "c@e.com",
                Ticket.TicketChannel.WEB_PORTAL, "cat1");
        String origTitle = entity.getTitle();
        TicketUpdateRequestDto dto = TicketUpdateRequestDto.builder().build();
        mapper.updateEntityFromDto(dto, entity);
        assertEquals(origTitle, entity.getTitle());
    }

    @Test
    void updateEntityFromDto_withWatchersAndRelated() {
        Ticket entity = Ticket.create("t1", "Title", "Desc",
                Ticket.TicketPriority.HIGH, "c1", "c@e.com",
                Ticket.TicketChannel.WEB_PORTAL, "cat1");
        TicketUpdateRequestDto dto = TicketUpdateRequestDto.builder()
                .watchers(List.of("w1"))
                .relatedTicketIds(List.of("r1"))
                .parentTicketId("p1")
                .customerRating(5)
                .customerFeedback("good")
                .build();
        mapper.updateEntityFromDto(dto, entity);
        assertEquals(List.of("w1"), entity.getWatchers());
        assertEquals(List.of("r1"), entity.getRelatedTicketIds());
        assertEquals("p1", entity.getParentTicketId());
        assertEquals(5, entity.getCustomerRating());
        assertEquals("good", entity.getCustomerFeedback());
    }
}
