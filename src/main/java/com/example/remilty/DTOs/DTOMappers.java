package com.example.remilty.DTOs;

import com.example.remilty.Models.SwiftData;

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

    public static SwiftCountryDataDTO mapToSwiftCountryDataDTO(SwiftData swiftData, List<SwiftDataDTO> branches) {
        return SwiftCountryDataDTO.builder()
                .countryISO2(swiftData.getCountryISO2())
                .countryName(swiftData.getCountryName())
                .branches(branches)
                .build();
    }
}
