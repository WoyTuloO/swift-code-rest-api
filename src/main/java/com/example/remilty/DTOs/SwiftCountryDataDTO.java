package com.example.remilty.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SwiftCountryDataDTO {
    private String countryISO2;
    private String countryName;
    private List<SwiftDataDTO> swiftCodes;
}
