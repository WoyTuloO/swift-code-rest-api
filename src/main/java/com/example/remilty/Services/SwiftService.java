package com.example.remilty.Services;

import com.example.remilty.DTOs.DTOMappers;
import com.example.remilty.DTOs.SwiftCountryDataDTO;
import com.example.remilty.DTOs.SwiftDataDTO;
import com.example.remilty.Models.SwiftData;
import com.example.remilty.Models.SwiftDataRequest;
import com.example.remilty.Repositories.SwiftRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SwiftService {

    private final SwiftRepository swiftRepository;

    @Autowired
    public SwiftService(SwiftRepository swiftRepository) {
        this.swiftRepository = swiftRepository;
    }

    public Optional<SwiftDataDTO> getSwiftDataBySwiftCode(String swiftCode) {

        List<SwiftData> allBySwiftCodeStartingWith = new ArrayList<>(
                swiftRepository.findAllBySwiftCodeStartingWith(
                        swiftCode.endsWith("XXX") ? swiftCode.substring(0, swiftCode.length() - 3).trim().toUpperCase() : swiftCode.trim().toUpperCase())
            );

        if(allBySwiftCodeStartingWith.isEmpty())
            return Optional.empty();

        SwiftData HQ = allBySwiftCodeStartingWith.stream()
                .filter(data -> data.getSwiftCode().equals(swiftCode.trim().toUpperCase()))
                .findFirst().orElse(allBySwiftCodeStartingWith.getFirst());

        List<SwiftDataDTO> branches = allBySwiftCodeStartingWith.stream()
                .filter(data->!data.getSwiftCode().equals(swiftCode.trim().toUpperCase()))
                .filter(data->!data.getSwiftCode().endsWith("XXX"))
                .map(data -> {
                    data.setCountryName(null);
                    return DTOMappers.mapToSwiftDataDTO(data, List.of());
                })
                .toList();

        return Optional.of(DTOMappers.mapToSwiftDataDTO(HQ, branches));

    }

    public Optional<SwiftCountryDataDTO> getSwiftDataByIso2Code(String iso2Code) {
        ArrayList<SwiftData> swiftData = new ArrayList<>(swiftRepository.findByCountryISO2(iso2Code.trim().toUpperCase()));

        if(swiftData.isEmpty())
            return Optional.empty();

        String countryName = swiftData.getFirst().getCountryName().trim().toUpperCase();
        String countryISO2 = swiftData.getFirst().getCountryISO2().trim().toUpperCase();

        List<SwiftDataDTO> swiftDataDTOList = swiftData.stream()
                .map(data -> {
                    data.setCountryName(null);
                    return DTOMappers.mapToSwiftDataDTO(data, null);
                })
                .toList();

        return Optional.of(DTOMappers.mapToSwiftCountryDataDTO(countryISO2, countryName, swiftDataDTOList));
    }

    public Long addSwiftData(SwiftDataRequest swiftDataRequest) {
        SwiftData saved = swiftRepository.save(DTOMappers.mapToSwiftData(swiftDataRequest));
        return saved.getId();
    }

    @Transactional
    public void deleteSwiftData(String swiftCode) {
        if(!swiftRepository.existsBySwiftCode(swiftCode.trim().toUpperCase()))
            throw new EntityNotFoundException("SWIFT code not found");

        swiftRepository.deleteBySwiftCode(swiftCode.trim().toUpperCase());
    }
}

