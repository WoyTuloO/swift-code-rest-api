package com.example.remilty.Models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class SwiftData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String countryISO2;
    @Column(unique = true, nullable = false)
    private String swiftCode;
    @Column(nullable = false)
    private String bankName;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String countryName;
    @Column(nullable = false)
    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter;


    public SwiftData(String iso2Code, String swiftCode, String name, String address, String countryName) {
        this.countryISO2 = iso2Code.trim().toUpperCase();
        this.swiftCode = swiftCode.trim().toUpperCase();
        this.bankName = name;
        this.address = address;
        isHeadquarter = swiftCode.endsWith("XXX");
        this.countryName = countryName.trim().toUpperCase();

    }


    public SwiftData() {
        this.countryISO2 = null;
        this.swiftCode = null;
        this.bankName = null;
        this.address = null;
        this.countryName = null;
    }

}
