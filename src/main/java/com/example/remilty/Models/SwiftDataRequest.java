package com.example.remilty.Models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwiftDataRequest {
    @NotBlank
    private String swiftCode;
    @NotBlank
    private String bankName;
    @NotBlank
    private String address;
    @NotBlank
    private String countryISO2;
    @NotBlank
    private String countryName;
}
