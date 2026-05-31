package com.gogidix.customersupport.livechat.application.dto;

import com.gogidix.customersupport.livechat.application.dto.ChatSessionResponseDto;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ChatSessionResponseDto_PreChatSurveyDataDtoTest {

        @Test
    void testBuilder() {
        ChatSessionResponseDto.PreChatSurveyDataDto dto = ChatSessionResponseDto.PreChatSurveyDataDto.builder()
                        .question1("test-question1")
            .answer1("test-answer1")
            .question2("test-question2")
            .answer2("test-answer2")
            .question3("test-question3")
            .answer3("test-answer3")
            .build();
        assertNotNull(dto);
        assertEquals("test-question1", dto.getQuestion1());
        assertEquals("test-answer1", dto.getAnswer1());
        assertEquals("test-question2", dto.getQuestion2());
        assertEquals("test-answer2", dto.getAnswer2());
        assertEquals("test-question3", dto.getQuestion3());
        assertEquals("test-answer3", dto.getAnswer3());
    }

    @Test
    void testSettersAndGetters() {
        ChatSessionResponseDto.PreChatSurveyDataDto dto = new ChatSessionResponseDto.PreChatSurveyDataDto();
        dto.setQuestion1("val-question1");
        dto.setAnswer1("val-answer1");
        dto.setQuestion2("val-question2");
        dto.setAnswer2("val-answer2");
        dto.setQuestion3("val-question3");
        dto.setAnswer3("val-answer3");
        assertEquals("val-question1", dto.getQuestion1());
        assertEquals("val-answer1", dto.getAnswer1());
        assertEquals("val-question2", dto.getQuestion2());
        assertEquals("val-answer2", dto.getAnswer2());
        assertEquals("val-question3", dto.getQuestion3());
        assertEquals("val-answer3", dto.getAnswer3());
    }

    @Test
    void testEqualsAndHashCode() {
        ChatSessionResponseDto.PreChatSurveyDataDto dto1 = ChatSessionResponseDto.PreChatSurveyDataDto.builder()
                        .question1("test-question1")
            .answer1("test-answer1")
            .question2("test-question2")
            .answer2("test-answer2")
            .question3("test-question3")
            .answer3("test-answer3")
            .build();
        ChatSessionResponseDto.PreChatSurveyDataDto dto2 = ChatSessionResponseDto.PreChatSurveyDataDto.builder()
                        .question1("test-question1")
            .answer1("test-answer1")
            .question2("test-question2")
            .answer2("test-answer2")
            .question3("test-question3")
            .answer3("test-answer3")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ChatSessionResponseDto.PreChatSurveyDataDto dto = ChatSessionResponseDto.PreChatSurveyDataDto.builder()
                        .question1("test-question1")
            .answer1("test-answer1")
            .question2("test-question2")
            .answer2("test-answer2")
            .question3("test-question3")
            .answer3("test-answer3")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}