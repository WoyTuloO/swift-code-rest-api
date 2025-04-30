package com.example.remilty.DTOs;

import com.example.remilty.Models.SwiftData;
import com.example.remilty.Models.SwiftDataRequest;

import java.util.List;

public class DTOMappers {

    public static SwiftDataDTO mapToSwiftDataDTO(SwiftData swiftData, List<SwiftDataDTO> branches) {
        return SwiftDataDTO.builder()
                .address(swiftData.getAddress())
                .bankName(swiftData.getBankName())
                .countryISO2(swiftData.getCountryISO2() ==null ? null : swiftData.getCountryISO2().trim().toUpperCase())
                .countryName(swiftData.getCountryName() == null ? null : swiftData.getCountryName().trim().toUpperCase())
                .isHeadquarter(swiftData.isHeadquarter())
                .swiftCode(swiftData.getSwiftCode().trim().toUpperCase())
                .branches(branches)
                .build();
    }

    public static SwiftCountryDataDTO mapToSwiftCountryDataDTO(String countryISO2, String countryName, List<SwiftDataDTO> swiftCodes) {
        return SwiftCountryDataDTO.builder()
                .countryISO2(countryISO2.trim().toUpperCase())
                .countryName(countryName.trim().toUpperCase())
                .swiftCodes(swiftCodes)
                .build();
    }

    public static SwiftData mapToSwiftData(SwiftDataRequest swiftDataRequest) {
        return SwiftData.builder()
                .address(swiftDataRequest.getAddress())
                .bankName(swiftDataRequest.getBankName())
                .countryISO2(swiftDataRequest.getCountryISO2().trim().toUpperCase())
                .countryName(swiftDataRequest.getCountryName().trim().toUpperCase())
                .isHeadquarter(swiftDataRequest.getSwiftCode().endsWith("XXX"))
                .swiftCode(swiftDataRequest.getSwiftCode().trim().toUpperCase())
                .build();
    }
}
