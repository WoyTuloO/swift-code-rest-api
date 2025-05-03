package com.example.remilty.Controllers;

import com.example.remilty.DTOs.SwiftCountryDataDTO;
import com.example.remilty.DTOs.SwiftDataDTO;
import com.example.remilty.Models.MessageResponse;
import com.example.remilty.Models.SwiftDataRequest;
import com.example.remilty.Services.SwiftService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "v1/swift-codes")
@Validated
public class SwiftController {

    private final SwiftService swiftService;

    @Autowired
    public SwiftController(SwiftService swiftService) {
        this.swiftService = swiftService;
    }

    @GetMapping("{swiftCode}")
    public ResponseEntity<Object> getSwiftCode(@PathVariable("swiftCode") String swiftCode) {
        if(swiftCode.length() != 11)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("SWIFT code must be exactly 11 characters"));

        Optional<SwiftDataDTO> swiftDataBySwiftCode = swiftService.getSwiftDataBySwiftCode(swiftCode.trim().toUpperCase());
        if (swiftDataBySwiftCode.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(swiftDataBySwiftCode.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No data found for the provided SWIFT code"));
    }

    @GetMapping("country/{iso2Code}")
    public ResponseEntity<Object> getSwiftCodeByIso2(@PathVariable ("iso2Code") String iso2Code) {
        Optional<SwiftCountryDataDTO> response = swiftService.getSwiftDataByIso2Code(iso2Code);
        if(response.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(response.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No data found for the provided ISO2 code"));
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

    @DeleteMapping("{swiftCode}")
    public ResponseEntity<MessageResponse> deleteSwiftData(@PathVariable("swiftCode") String swiftCode) {
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
