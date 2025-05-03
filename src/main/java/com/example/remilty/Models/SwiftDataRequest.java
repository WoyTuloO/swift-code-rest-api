package com.example.remilty.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwiftDataRequest {
    @Size(min = 11, max = 11, message = "SWIFT code must be exactly 11 characters")
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
    @NotNull
    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter;
}
