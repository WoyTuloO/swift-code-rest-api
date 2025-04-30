package com.example.remilty.Controllers;

import com.example.remilty.DTOs.DTOMappers;
import com.example.remilty.DTOs.SwiftCountryDataDTO;
import com.example.remilty.DTOs.SwiftDataDTO;
import com.example.remilty.Models.MessageResponse;
import com.example.remilty.Models.SwiftData;
import com.example.remilty.Models.SwiftDataRequest;
import com.example.remilty.Services.SwiftService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "v1/swift-codes")
public class SwiftController {

    private final SwiftService swiftService;

    @Autowired
    public SwiftController(SwiftService swiftService) {
        this.swiftService = swiftService;
    }

    @GetMapping("{swift-code}")
    public ResponseEntity<SwiftDataDTO> getSwiftCode(@PathVariable("swift-code") String swiftCode) {

        List<SwiftData> swiftDataBySwiftCode = swiftService.getSwiftDataBySwiftCode(swiftCode);
        if(swiftDataBySwiftCode.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if(swiftDataBySwiftCode.size() == 1) {
            return ResponseEntity.ok(DTOMappers.mapToSwiftDataDTO(swiftDataBySwiftCode.getFirst(), null));
        }

        SwiftData hq = swiftDataBySwiftCode.stream().filter(data -> data.getSwiftCode().equals(swiftCode)).toList().getFirst();

        List<SwiftDataDTO> branches = swiftDataBySwiftCode.stream().filter(data -> !data.getSwiftCode().endsWith("XXX"))
                .map(data -> {
                    data.setCountryName(null);
                    return DTOMappers.mapToSwiftDataDTO(data, null);
                })
                .toList();
        if(branches.isEmpty())
            branches = null;


        SwiftDataDTO swiftDataDTOhq = DTOMappers.mapToSwiftDataDTO(hq, branches);

        return ResponseEntity.ok(swiftDataDTOhq);
    }

    @GetMapping("country/{iso2-code}")
    public ResponseEntity<SwiftCountryDataDTO> getSwiftCodeByIso2(@PathVariable ("iso2-code") String iso2Code) {
        List<SwiftData> swiftDataByIso2CodeList = swiftService.getSwiftDataByIso2Code(iso2Code);

        if(swiftDataByIso2CodeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String countryName = swiftDataByIso2CodeList.getFirst().getCountryName();
        String countryISO2 = swiftDataByIso2CodeList.getFirst().getCountryISO2();

        List<SwiftDataDTO> swiftDataDTOList = swiftDataByIso2CodeList.stream()
                .map(data -> {
                    data.setCountryName(null);
                    return DTOMappers.mapToSwiftDataDTO(data, null);
                })
                .toList();
        SwiftCountryDataDTO swiftCountryDataDTO = DTOMappers.mapToSwiftCountryDataDTO(countryISO2, countryName, swiftDataDTOList);

        return ResponseEntity.ok(swiftCountryDataDTO);
    }

    @PostMapping()
    public ResponseEntity<MessageResponse> addSwiftData(@RequestBody @Valid SwiftDataRequest swiftDataRequest) {
        try {
            Long postId = swiftService.addSwiftData(swiftDataRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Data saved successfully with ID: " + postId));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("SWIFT code already exists"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Unexpected error occurred"));
        }
    }

    @DeleteMapping("{swift-code}")
    public ResponseEntity<MessageResponse> deleteSwiftData(@PathVariable("swift-code") String swiftCode) {
        try {
            swiftService.deleteSwiftData(swiftCode);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("SWIFT code deleted successfully"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("SWIFT code not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Unexpected error occurred"));
        }
    }



}
