package com.example.remilty.Controllers;

import com.example.remilty.Models.SwiftData;
import com.example.remilty.Models.SwiftDataRequest;
import com.example.remilty.Repositories.SwiftRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SwiftControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {
        randomSwiftCode = generateRandomSwiftCode();
    }

    @Autowired
    private SwiftRepository swiftRepository;

    @BeforeEach
    void initData() {
        swiftRepository.deleteAll();

        swiftRepository.saveAll(List.of(
                new SwiftData("PL", "KOPWPLP1XXX", "KOMPANIA PIWOWARSKA S.A.", "UL. SZWAJCARSKA  POZNAN, WIELKOPOLSKIE, 61-285", "POLAND"),
                new SwiftData("PL", "KOPWPLP1KPW", "KOMPANIA PIWOWARSKA S.A.", "  POZNAN, WIELKOPOLSKIE", "POLAND"),
                new SwiftData("LV", "AIZKLV22XXX", "ABLV BANK, AS IN LIQUIDATION", "MIHAILA TALA STREET 1  RIGA, RIGA, LV-1045", "LATVIA"),
                new SwiftData("LV", "BLPBLV21XXX", "AS BALTIJAS PRIVATBANKA", "SIA MONO KATLAKALNA ST. 1 RIGA, RIGA, LV-1017", "LATVIA")
        ));
    }

    static String randomSwiftCode;
    @Test
    void shouldReturnSwiftDataBySwiftCode() throws Exception {
        String swiftCode2 = "KOPWPLP1XXX";
        mockMvc.perform(get("/v1/swift-codes/{swiftCode}", swiftCode2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value(swiftCode2))
                .andExpect(jsonPath("$.branches[0].swiftCode").value("KOPWPLP1KPW"));

    }

    @Test
    void shouldNotReturnSwiftDataByInvalidSwiftCode() throws Exception {
        String swiftCode = "ABCDUS33";
        mockMvc.perform(get("/v1/swift-codes/{swiftCode}", swiftCode))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No data found for the provided SWIFT code"));
    }

    @Test
    void shouldReturnSwiftDataByIso2Code() throws Exception {
        String invalidIso2Code = "LV";
        mockMvc.perform(get("/v1/swift-codes/country/{iso2Code}", invalidIso2Code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value("LV"))
                .andExpect(jsonPath("$.countryName").value("LATVIA"))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode").value("AIZKLV22XXX"))
                .andExpect(jsonPath("$.swiftCodes[0].address").value("MIHAILA TALA STREET 1  RIGA, RIGA, LV-1045"))
                .andExpect(jsonPath("$.swiftCodes[0].bankName").value("ABLV BANK, AS IN LIQUIDATION"))
                .andExpect(jsonPath("$.swiftCodes[0].countryISO2").value("LV"))
                .andExpect(jsonPath("$.swiftCodes[0].isHeadquarter").value(true))
                .andExpect(jsonPath("$.swiftCodes[1].swiftCode").value("BLPBLV21XXX"))
                .andExpect(jsonPath("$.swiftCodes[1].address").value("SIA MONO KATLAKALNA ST. 1 RIGA, RIGA, LV-1017"))
                .andExpect(jsonPath("$.swiftCodes[1].bankName").value("AS BALTIJAS PRIVATBANKA"))
                .andExpect(jsonPath("$.swiftCodes[1].countryISO2").value("LV"))
                .andExpect(jsonPath("$.swiftCodes[1].isHeadquarter").value(true));

    }

    @Test
    void shouldNotReturnSwiftDataByInvalidIso2Code() throws Exception {
        String iso2Code = "US";
        mockMvc.perform(get("/v1/swift-codes/country/{iso2Code}", iso2Code))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No data found for the provided ISO2 code"));
    }

    @Test
    void shouldAddSwiftData_and_shouldReturnConflictWhenDuplicatingData() throws Exception {
        SwiftDataRequest request = new SwiftDataRequest(
                randomSwiftCode, "US", "United States", "123 Test St, Test City, Test State, 12345", "ABCDUS33"        );
        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", startsWith("Data saved successfully with ID: ")));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("SWIFT code already exists"));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentSwiftData() throws Exception {
        String swiftCode = "NONEXISTENTCODE";
        mockMvc.perform(delete("/v1/swift-codes/{swiftCode}", swiftCode))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SWIFT code not found"));
    }

    @Test
    void shouldDeleteSwiftData() throws Exception {
        String swiftCode = "KOPWPLP1XXX";
        mockMvc.perform(delete("/v1/swift-codes/{swiftCode}", swiftCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code deleted successfully"));
    }


    private static String generateRandomSwiftCode() {
        StringBuilder swiftCode = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randomChar = (int) (Math.random() * 26);
            char c = (char) ('A' + randomChar);
            swiftCode.append(c);
        }
        return swiftCode.toString();
    }


}
