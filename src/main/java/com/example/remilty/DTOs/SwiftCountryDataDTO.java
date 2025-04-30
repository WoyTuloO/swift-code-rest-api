package com.example.remilty.DTOs;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SwiftCountryDataDTO {
    private String countryISO2;
    private String countryName;
    private List<SwiftDataDTO> branches;
}
