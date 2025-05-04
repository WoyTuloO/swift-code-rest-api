package com.example.remilty.Controllers;

import com.example.remilty.DTOs.SwiftCountryDataDTO;
import com.example.remilty.DTOs.SwiftDataDTO;
import com.example.remilty.Models.SwiftDataRequest;
import com.example.remilty.Services.SwiftService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SwiftController.class)
public class SwiftControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SwiftService swiftService;

    @Test
    void getExistingSwiftCode() throws Exception {
        SwiftDataDTO dto = SwiftDataDTO.builder()
                .swiftCode("ABCDEF12XXX")
                .bankName("Test Bank")
                .address("Test Address")
                .countryISO2("PL")
                .countryName("POLAND")
                .isHeadquarter(true)
                .branches(List.of())
                .build();

        Mockito.when(swiftService.getSwiftDataBySwiftCode("ABCDEF12XXX"))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(get("/v1/swift-codes/{swiftCode}", "ABCDEF12XXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("ABCDEF12XXX"))
                .andExpect(jsonPath("$.isHeadquarter").value(true));

        verify(swiftService).getSwiftDataBySwiftCode("ABCDEF12XXX");

    }

    @Test
    void getInvalidSwiftCode() throws Exception {
        String invalidSwiftCode = "INVALI";
        when(swiftService.getSwiftDataBySwiftCode(invalidSwiftCode)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/swift-codes/{swiftCode}", invalidSwiftCode))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("SWIFT code must be exactly 11 characters"));

    }

    @Test
    void getExistingCountry() throws Exception {
        SwiftDataDTO code1 = SwiftDataDTO.builder().swiftCode("ABCUS33XXX").build();

        SwiftCountryDataDTO countryDataDTO = SwiftCountryDataDTO.builder()
                .countryISO2("US")
                .countryName("UNITED STATES")
                .swiftCodes(List.of(code1))
                .build();

        when(swiftService.getSwiftDataByIso2Code("US")).thenReturn(Optional.of(countryDataDTO));

        mockMvc.perform(get("/v1/swift-codes/country/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value("US"))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode").value("ABCUS33XXX"));

        verify(swiftService).getSwiftDataByIso2Code("US");
    }


    @Test
    void getNonExistingSwiftCode() throws Exception {
        String swiftCode = "WRONGSWIFTT";
        when(swiftService.getSwiftDataBySwiftCode(swiftCode)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/swift-codes/{swiftCode}", swiftCode))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No data found for the provided SWIFT code"));

        verify(swiftService).getSwiftDataBySwiftCode(swiftCode);
    }

    @Test
    void getNonExistingCountry() throws Exception {
        String iso2Code = "WRONGISO2";
        when(swiftService.getSwiftDataByIso2Code(iso2Code)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/swift-codes/country/{iso2Code}", iso2Code))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No data found for the provided ISO2 code"));

        verify(swiftService).getSwiftDataByIso2Code(iso2Code);
    }

    @Test
    void postValidSwiftData() throws Exception {
        SwiftDataRequest req = new SwiftDataRequest(
                "ABCDEF12XXX",
                "Test Bank",
                "Test Address",
                "PL",
                "POLAND",
                true
        );

        when(swiftService.addSwiftData(any(SwiftDataRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Data saved successfully")));

        verify(swiftService).addSwiftData(any(SwiftDataRequest.class));

    }

    @Test
    void postDuplicatedSwiftData() throws Exception {
        SwiftDataRequest req = new SwiftDataRequest(
                "ABCDEF12XXX",
                "Test Bank",
                "Test Address",
                "PL",
                "POLAND",
                true
        );

        when(swiftService.addSwiftData(any(SwiftDataRequest.class))).thenThrow(new DataIntegrityViolationException("SWIFT code already exists"));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("SWIFT code already exists"));

    }

    @Test
    void postInvalidSwiftData() throws Exception {
        SwiftDataRequest req = new SwiftDataRequest(
                "ABC2XXX",
                "Test Bank",
                "Test Address",
                "PL",
                "POLAND",
                true
        );


        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isBadRequest());

    }


    @Test
    void postSwiftDataWithUnexpectedError() throws Exception {
        SwiftDataRequest req = new SwiftDataRequest(
                "ABCDEF12XXX",
                "Test Bank",
                "Test Address",
                "PL",
                "POLAND",
                true
        );

        when(swiftService.addSwiftData(any(SwiftDataRequest.class))).thenThrow(new RuntimeException("Unexpected error occurred"));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"));

    }

    @Test
    void deleteExistingSwiftCode() throws Exception {
        doNothing().when(swiftService).deleteSwiftData(anyString());
        mockMvc.perform(delete("/v1/swift-codes/ABCDXXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code deleted successfully"));

        verify(swiftService).deleteSwiftData("ABCDXXX");
    }

    @Test
    void deleteNonExistingSwiftCode() throws Exception {
        doThrow(new EntityNotFoundException("SWIFT code not found")).when(swiftService).deleteSwiftData(anyString());

        mockMvc.perform(delete("/v1/swift-codes/WRONGCODE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SWIFT code not found"));

        verify(swiftService).deleteSwiftData("WRONGCODE");
    }

    @Test
    void deleteSwiftCodeWithUnexpectedError() throws Exception {
        doThrow(new RuntimeException("Unexpected error")).when(swiftService).deleteSwiftData(anyString());

        mockMvc.perform(delete("/v1/swift-codes/ABCDXXX"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"));

        verify(swiftService).deleteSwiftData("ABCDXXX");
    }

}
