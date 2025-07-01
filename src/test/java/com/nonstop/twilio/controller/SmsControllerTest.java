package com.nonstop.twilio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nonstop.twilio.dto.SmsRequest;
import com.nonstop.twilio.dto.SmsResponse;
import com.nonstop.twilio.service.SmsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for SmsController.
 */
@WebMvcTest(SmsController.class)
class SmsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SmsService smsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendSms_Success() throws Exception {
        SmsRequest request = new SmsRequest("+1234567890", "Test message");
        SmsResponse response = SmsResponse.success(
            "test-sid", "+1234567890", "+0987654321", 
            "Test message", "sent", LocalDateTime.now()
        );

        when(smsService.sendSms(any(SmsRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/sms/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.sid").value("test-sid"));
    }

    @Test
    void sendSms_InvalidPhoneNumber() throws Exception {
        SmsRequest request = new SmsRequest("invalid-phone", "Test message");

        mockMvc.perform(post("/api/v1/sms/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendSms_EmptyMessage() throws Exception {
        SmsRequest request = new SmsRequest("+1234567890", "");

        mockMvc.perform(post("/api/v1/sms/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void health_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/sms/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("SMS Service is running"));
    }
}
