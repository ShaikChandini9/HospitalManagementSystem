package com.hospital.patientservice.exception;

import com.hospital.patientservice.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Patient not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNotFound(ex);

        assertNotNull(response);
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Patient not found", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void testHandleGenericException() {
        RuntimeException ex = new RuntimeException("Something went wrong");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGeneric(ex);

        assertNotNull(response);
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Something went wrong", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void testHandleValidationErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("patientRequest", "fullName", "Full name is required");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationErrors(ex);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("fullName"));
        assertEquals("Full name is required", response.getBody().get("fullName"));
    }
}
