package com.example.remilty.Repositories;

import com.example.remilty.Models.SwiftData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class SwiftRepositoryTest {

    @Autowired SwiftRepository swiftRepository;

    @BeforeEach
    void setUpData() {
        swiftRepository.deleteAll();
        swiftRepository.saveAll(List.of(
                new SwiftData("PL", "KOPWPLP1XXX", "KOMPANIA PIWOWARSKA S.A.", "UL. SZWAJCARSKA  POZNAN, WIELKOPOLSKIE, 61-285", "POLAND"),
                new SwiftData("PL", "KOPWPLP1KPW", "KOMPANIA PIWOWARSKA S.A.", "  POZNAN, WIELKOPOLSKIE", "POLAND"),
                new SwiftData("LV", "AIZKLV22XXX", "ABLV BANK, AS IN LIQUIDATION", "MIHAILA TALA STREET 1  RIGA, RIGA, LV-1045", "LATVIA"),
                new SwiftData("LV", "BLPBLV21XXX", "AS BALTIJAS PRIVATBANKA", "SIA MONO KATLAKALNA ST. 1 RIGA, RIGA, LV-1017", "LATVIA")
        ));
    }

    @Test
    void shouldFindByCountryISO2() {
        String countryISO2 = "PL";
        List<SwiftData> swiftDataList = swiftRepository.findByCountryISO2(countryISO2);
        assertThat(swiftDataList).hasSize(2);
        assertThat(swiftDataList.getFirst().getCountryISO2()).isEqualTo(countryISO2);
    }

    @Test
    void shouldNotFindByInvalidCountryISO2() {
        String invalidCountryISO2 = "XX";
        List<SwiftData> swiftDataList = swiftRepository.findByCountryISO2(invalidCountryISO2);
        assertThat(swiftDataList).isEmpty();
    }

    @Test
    void shouldFindAllBySwiftCodeStartingWith() {
        String swiftCodePrefix = "KOPWPLP1KPW";
        List<SwiftData> swiftDataList = swiftRepository.findAllBySwiftCodeStartingWith(swiftCodePrefix);
        assertThat(swiftDataList).hasSize(1);
        assertThat(swiftDataList.getFirst().getSwiftCode()).isEqualTo("KOPWPLP1KPW");
    }

    @Test
    void shouldNotFindAllByInvalidSwiftCodePrefix() {
        String invalidSwiftCodePrefix = "KOPWPLP1ZZZ";
        List<SwiftData> swiftDataList = swiftRepository.findAllBySwiftCodeStartingWith(invalidSwiftCodePrefix);
        assertThat(swiftDataList).isEmpty();
    }

    @Test
    void shouldCheckIfSwiftCodeExists() {
        String swiftCode = "KOPWPLP1XXX";
        boolean exists = swiftRepository.existsBySwiftCode(swiftCode);
        assertThat(exists).isTrue();
    }

    @Test
    void shouldNotFindByInvalidSwiftCode() {
        String invalidSwiftCode = "WHATEVER";
        boolean exists = swiftRepository.existsBySwiftCode(invalidSwiftCode);
        assertThat(exists).isFalse();
    }

    @Test
    void shouldNotCheckIfInvalidSwiftCodeExists() {
        String invalidSwiftCode = "KOPWPLP1ZZZ";
        boolean exists = swiftRepository.existsBySwiftCode(invalidSwiftCode);
        assertThat(exists).isFalse();
    }

    @Test
    void shouldDeleteBySwiftCode() {
        String swiftCode = "KOPWPLP1XXX";
        swiftRepository.deleteBySwiftCode(swiftCode);
        boolean exists = swiftRepository.existsBySwiftCode(swiftCode);
        assertThat(exists).isFalse();
    }

    @Test
    void shouldNotDeleteByInvalidSwiftCode() {
        String invalidSwiftCode = "KOPWPLP1ZZZ";
        swiftRepository.deleteBySwiftCode(invalidSwiftCode);
        boolean exists = swiftRepository.existsBySwiftCode(invalidSwiftCode);
        assertThat(exists).isFalse();
    }

}
