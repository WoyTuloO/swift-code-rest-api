package com.example.remilty.DTOs;

import com.example.remilty.Models.SwiftData;
import com.example.remilty.Models.SwiftDataRequest;

import java.util.List;

public class DTOMappers {

    public static SwiftDataDTO mapToSwiftDataDTO(SwiftData swiftData, List<SwiftDataDTO> branches) {
        return SwiftDataDTO.builder()
                .address(swiftData.getAddress())
                .bankName(swiftData.getBankName())
                .countryISO2(swiftData.getCountryISO2())
                .countryName(swiftData.getCountryName())
                .isHeadquarter(swiftData.isHeadquarter())
                .swiftCode(swiftData.getSwiftCode())
                .branches(branches)
                .build();
    }

    public static SwiftCountryDataDTO mapToSwiftCountryDataDTO(String countryISO2, String countryName, List<SwiftDataDTO> branches) {
        return SwiftCountryDataDTO.builder()
                .countryISO2(countryISO2)
                .countryName(countryName)
                .branches(branches)
                .build();
    }

    public static SwiftData mapToSwiftData(SwiftDataRequest swiftDataRequest) {
        return SwiftData.builder()
                .address(swiftDataRequest.getAddress())
                .bankName(swiftDataRequest.getBankName())
                .countryISO2(swiftDataRequest.getCountryISO2())
                .countryName(swiftDataRequest.getCountryName())
                .swiftCode(swiftDataRequest.getSwiftCode())
                .build();
    }
}
