package com.example.remilty.Services;

import com.example.remilty.DTOs.DTOMappers;
import com.example.remilty.DTOs.SwiftCountryDataDTO;
import com.example.remilty.DTOs.SwiftDataDTO;
import com.example.remilty.Models.SwiftData;
import com.example.remilty.Models.SwiftDataRequest;
import com.example.remilty.Repositories.SwiftRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class SwiftServiceTest {
    @Mock
    private SwiftRepository swiftRepository;
    private SwiftService underTest;

    private SwiftData hq;
    private SwiftData branch1;
    private SwiftData branch2;


    @BeforeEach
    void setUp() {
        underTest = new SwiftService(swiftRepository);
        hq = new SwiftData("PL", "BANKPLPPXXX", "HQ", "HQ", "POLAND");
        branch1 = new SwiftData("PL", "BANKPLPP01A", "Branch1", "1", "POLAND");
        branch2 = new SwiftData("PL", "BANKPLPP02B", "Branch2", "2", "POLAND");
    }

    @Test
    void shallGetHQAndBranches() {

        given(swiftRepository.findAllBySwiftCodeStartingWith(hq.getSwiftCode().substring(0, hq.getSwiftCode().length() - 3)))
                .willReturn(List.of(hq, branch1, branch2));

        Optional<SwiftDataDTO> result = underTest.getSwiftDataBySwiftCode(hq.getSwiftCode());

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getSwiftCode()).isEqualTo(hq.getSwiftCode());
        assertThat(result.get().getBranches()).hasSize(2);
        assertThat(result.get().isHeadquarter()).isTrue();
        then(swiftRepository).should().findAllBySwiftCodeStartingWith(hq.getSwiftCode().substring(0, hq.getSwiftCode().length() - 3));
    }


    @Test
    void shallGetBankHQ(){
        given(swiftRepository.findAllBySwiftCodeStartingWith(hq.getSwiftCode().substring(0, hq.getSwiftCode().length() - 3))).willReturn(List.of(hq));

        Optional<SwiftDataDTO> swiftDataBySwiftCode = underTest.getSwiftDataBySwiftCode(hq.getSwiftCode());

        assertThat(swiftDataBySwiftCode).isPresent();

        SwiftDataDTO expected = DTOMappers.mapToSwiftDataDTO(hq, List.of());
        SwiftDataDTO actual = swiftDataBySwiftCode.get();

        assertThat(actual.getSwiftCode()).isEqualTo(expected.getSwiftCode());
        assertThat(actual.getBranches()).isEmpty();
        assertThat(actual.isHeadquarter()).isTrue();

        then(swiftRepository).should().findAllBySwiftCodeStartingWith(hq.getSwiftCode().substring(0, hq.getSwiftCode().length() - 3));
    }

    @Test
    void shallGetBankBranch(){
        given(swiftRepository.findAllBySwiftCodeStartingWith(branch1.getSwiftCode())).willReturn(List.of(branch1));
        Optional<SwiftDataDTO> swiftDataBySwiftCode = underTest.getSwiftDataBySwiftCode(branch1.getSwiftCode());

        assertThat(swiftDataBySwiftCode).isPresent();

        SwiftDataDTO expected = DTOMappers.mapToSwiftDataDTO(branch1, List.of());
        SwiftDataDTO actual = swiftDataBySwiftCode.get();

        assertThat(actual.getSwiftCode()).isEqualTo(expected.getSwiftCode());
        assertThat(actual.getBranches()).isEmpty();
        assertThat(actual.isHeadquarter()).isFalse();
    }

    @Test
    void shallGetEmptyList_NoHQ(){
        given(swiftRepository.findAllBySwiftCodeStartingWith("WHATEVER")).willReturn(List.of());
        assertThat(underTest.getSwiftDataBySwiftCode("WHATEVER")).isEmpty();
        then(swiftRepository).should().findAllBySwiftCodeStartingWith("WHATEVER");
    }

    @Test
    void shallGetEmptyList_HQ(){
        given(swiftRepository.findAllBySwiftCodeStartingWith("WHATEXXX".substring(0, 5))).willReturn(List.of());
        assertThat(underTest.getSwiftDataBySwiftCode("WHATEXXX")).isEmpty();
        then(swiftRepository).should().findAllBySwiftCodeStartingWith("WHATEXXX".substring(0, 5));
    }

    @Test
    void shallAddNewSwiftData(){
        SwiftDataRequest req = new SwiftDataRequest();
        req.setSwiftCode("BANKPLPPXXX");
        req.setBankName("BANK");
        req.setAddress("HQ");
        req.setCountryName("POLAND");
        req.setCountryISO2("PL");

        SwiftData swiftData = new SwiftData("PL", "BANKPLPPXXX", "BANK", "HQ", "POLAND");
        swiftData.setId(15L);
        given(swiftRepository.save(any(SwiftData.class))).willReturn(swiftData);

        Long id = underTest.addSwiftData(req);

        assertThat(id).isEqualTo(15L);

        then(swiftRepository).should().save(argThat(e->
                e.getSwiftCode().equals(req.getSwiftCode()) &&
                e.getBankName().equals(req.getBankName()) &&
                e.getAddress().equals(req.getAddress()) &&
                e.getCountryName().equals(req.getCountryName()) &&
                e.getCountryISO2().equals(req.getCountryISO2()) &&
                e.isHeadquarter()
        ));
    }

    @Test
    void shallReturnSwiftDataCountryDTO_exists(){
        SwiftData d1 = new SwiftData("PL", "BANKPLPPXXX", "BANK", "HQ", "POLAND");
        SwiftData d2 = new SwiftData("PL", "BANKPLPP01A", "Branch1", "1", "POLAND");
        SwiftData d3 = new SwiftData("PL", "BANKPLPP02B", "Branch2", "2", "POLAND");

        given(swiftRepository.findByCountryISO2("PL")).willReturn(List.of(d1, d2, d3));

        Optional<SwiftCountryDataDTO> result = underTest.getSwiftDataByIso2Code("PL");

        assertThat(result).isPresent();
        SwiftCountryDataDTO resultDto = result.get();

        assertThat(resultDto.getCountryISO2()).isEqualTo("PL");
        assertThat(resultDto.getCountryName()).isEqualTo("POLAND");
        assertThat(resultDto.getSwiftCodes()).hasSize(3);
        assertThat(resultDto.getSwiftCodes().get(0).getSwiftCode()).isEqualTo("BANKPLPPXXX");
        assertThat(resultDto.getSwiftCodes().get(1).getSwiftCode()).isEqualTo("BANKPLPP01A");
        assertThat(resultDto.getSwiftCodes().get(2).getSwiftCode()).isEqualTo("BANKPLPP02B");

        assertThat(resultDto.getSwiftCodes().get(0).isHeadquarter()).isTrue();
        assertThat(resultDto.getSwiftCodes().get(1).isHeadquarter()).isFalse();
        assertThat(resultDto.getSwiftCodes().get(2).isHeadquarter()).isFalse();

        then(swiftRepository).should().findByCountryISO2("PL");

    }

    @Test
    void shallReturnSwiftDataCountryDTO_notExists(){
        given(swiftRepository.findByCountryISO2("PL")).willReturn(List.of());

        Optional<SwiftCountryDataDTO> result = underTest.getSwiftDataByIso2Code("PL");

        assertThat(result).isEmpty();

        then(swiftRepository).should().findByCountryISO2("PL");
    }

    @Test
    void shallAddNewSwiftData_Duplicate(){
        SwiftDataRequest req = new SwiftDataRequest();
        req.setSwiftCode("BANKPLPPXXX");
        req.setBankName("BANK");
        req.setAddress("HQ");
        req.setCountryName("POLAND");
        req.setCountryISO2("PL");

        SwiftData swiftData = new SwiftData("PL", "BANKPLPPXXX", "BANK", "HQ", "POLAND");
        swiftData.setId(15L);
        given(swiftRepository.save(any(SwiftData.class))).willThrow(new DataIntegrityViolationException("duplicate"));

        assertThrows(DataIntegrityViolationException.class, () -> underTest.addSwiftData(req));
        then(swiftRepository).should().save(any(SwiftData.class));
    }

    @Test
    void shallDeleteSwiftData(){
        given(swiftRepository.existsBySwiftCode(hq.getSwiftCode())).willReturn(true);
        underTest.deleteSwiftData(hq.getSwiftCode());

        then(swiftRepository).should().existsBySwiftCode(hq.getSwiftCode());
        then(swiftRepository).should().deleteBySwiftCode(hq.getSwiftCode());
    }

    @Test
    void shallDeleteSwiftData_NotFound(){
        given(swiftRepository.existsBySwiftCode(hq.getSwiftCode())).willReturn(false);
        assertThrows(EntityNotFoundException.class, ()->underTest.deleteSwiftData(hq.getSwiftCode()));

        then(swiftRepository).should().existsBySwiftCode(hq.getSwiftCode());
        then(swiftRepository).should(never()).deleteBySwiftCode(hq.getSwiftCode());
    }

}
