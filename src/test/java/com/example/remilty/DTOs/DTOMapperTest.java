package com.example.remilty.DTOs;

import com.example.remilty.Models.SwiftData;
import com.example.remilty.Models.SwiftDataRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DTOMapperTest {

    @Test
    void shouldMapToSwiftDataDTO_WithBranches(){
        SwiftData main = SwiftData.builder()
                .address("123 Main St")
                .bankName("Bank of America")
                .countryISO2("US")
                .countryName("United States")
                .isHeadquarter(true)
                .swiftCode("BOFAUS3NXXX")
                .build();

        SwiftDataDTO branch1 = SwiftDataDTO.builder()
                .address("456 Elm St")
                .bankName("Bank of America")
                .countryISO2("US")
                .countryName("United States")
                .isHeadquarter(false)
                .swiftCode("BOFAUS3NBR1")
                .build();

        SwiftDataDTO branch2 = SwiftDataDTO.builder()
                .address("789 Oak St")
                .bankName("Bank of America")
                .countryISO2("US")
                .countryName("United States")
                .isHeadquarter(false)
                .swiftCode("BOFAUS3NBR2")
                .build();

        List<SwiftDataDTO> branches = List.of(branch1, branch2);

        SwiftDataDTO result = DTOMappers.mapToSwiftDataDTO(main, branches);

        assertThat(result.getAddress()).isEqualTo("123 Main St");
        assertThat(result.getBankName()).isEqualTo("Bank of America");
        assertThat(result.getCountryISO2()).isEqualTo("US");
        assertThat(result.getCountryName()).isEqualTo("UNITED STATES");
        assertThat(result.isHeadquarter()).isTrue();
        assertThat(result.getSwiftCode()).isEqualTo("BOFAUS3NXXX");
        assertThat(result.getBranches()).isEqualTo(branches);


    }

    @Test
    void shouldMapToSwiftDataDTO_WithoutBranches(){
        SwiftData main = SwiftData.builder()
                .address("123 Main St")
                .bankName("Bank of America")
                .countryISO2("US")
                .countryName("United States")
                .isHeadquarter(true)
                .swiftCode("BOFAUS3NXXX")
                .build();

        List<SwiftDataDTO> branches = List.of();

        SwiftDataDTO result = DTOMappers.mapToSwiftDataDTO(main, branches);

        assertThat(result.getAddress()).isEqualTo("123 Main St");
        assertThat(result.getBankName()).isEqualTo("Bank of America");
        assertThat(result.getCountryISO2()).isEqualTo("US");
        assertThat(result.getCountryName()).isEqualTo("UNITED STATES");
        assertThat(result.isHeadquarter()).isTrue();
        assertThat(result.getSwiftCode()).isEqualTo("BOFAUS3NXXX");
        assertThat(result.getBranches()).isEqualTo(branches);
    }

    @Test
    void shouldMapToSwiftCountryDataDTO() {
        String countryISO2 = "US";
        String countryName = "United States";

        SwiftDataDTO swiftCode1 = SwiftDataDTO.builder()
                .swiftCode("BOFAUS3NXXX")
                .build();

        SwiftDataDTO swiftCode2 = SwiftDataDTO.builder()
                .swiftCode("BODAKS3NXXX")
                .build();

        List<SwiftDataDTO> swiftCodes = List.of(swiftCode1, swiftCode2);

        SwiftCountryDataDTO result = DTOMappers.mapToSwiftCountryDataDTO(countryISO2, countryName, swiftCodes);

        assertThat(result.getCountryISO2()).isEqualTo("US");
        assertThat(result.getCountryName()).isEqualTo("UNITED STATES");
        assertThat(result.getSwiftCodes()).isEqualTo(swiftCodes);


    }

    @Test
    void shouldMapToSwiftData_True(){
        SwiftDataRequest swiftDataRequest = new SwiftDataRequest();
        swiftDataRequest.setAddress("123 Main St");
        swiftDataRequest.setBankName("Bank of America");
        swiftDataRequest.setCountryISO2("US");
        swiftDataRequest.setCountryName("United States");
        swiftDataRequest.setSwiftCode("BOFAUS3NXXX");
        swiftDataRequest.setHeadquarter(true);

        SwiftData result = DTOMappers.mapToSwiftData(swiftDataRequest);
        assertThat(result.getAddress()).isEqualTo("123 Main St");
        assertThat(result.getBankName()).isEqualTo("Bank of America");
        assertThat(result.getCountryISO2()).isEqualTo("US");
        assertThat(result.getCountryName()).isEqualTo("UNITED STATES");
        assertThat(result.isHeadquarter()).isTrue();
        assertThat(result.getSwiftCode()).isEqualTo("BOFAUS3NXXX");

    }

    @Test
    void shouldMapToSwiftData_False(){
        SwiftDataRequest swiftDataRequest = new SwiftDataRequest();
        swiftDataRequest.setAddress("123 Main St");
        swiftDataRequest.setBankName("Bank of America");
        swiftDataRequest.setCountryISO2("US");
        swiftDataRequest.setCountryName("United States");
        swiftDataRequest.setSwiftCode("BOFAUS3NDDD");
        swiftDataRequest.setHeadquarter(false);

        SwiftData result = DTOMappers.mapToSwiftData(swiftDataRequest);
        assertThat(result.getAddress()).isEqualTo("123 Main St");
        assertThat(result.getBankName()).isEqualTo("Bank of America");
        assertThat(result.getCountryISO2()).isEqualTo("US");
        assertThat(result.getCountryName()).isEqualTo("UNITED STATES");
        assertThat(result.isHeadquarter()).isFalse();
        assertThat(result.getSwiftCode()).isEqualTo("BOFAUS3NDDD");

    }
}
