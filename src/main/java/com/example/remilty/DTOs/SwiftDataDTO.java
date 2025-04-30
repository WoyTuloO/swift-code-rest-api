package com.example.remilty.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@EqualsAndHashCode
@AllArgsConstructor
public class SwiftDataDTO {
    String address;
    String bankName;
    String countryISO2;
    String countryName;
    @JsonProperty("isHeadquarter")
    boolean isHeadquarter;
    String swiftCode;
    List<SwiftDataDTO> branches;
}

