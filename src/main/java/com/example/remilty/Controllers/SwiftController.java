package com.example.remilty.Controllers;

import com.example.remilty.DTOs.DTOMappers;
import com.example.remilty.DTOs.SwiftDataDTO;
import com.example.remilty.Models.SwiftData;
import com.example.remilty.Services.SwiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<String> getSwiftCodeByIso2(@PathVariable ("iso2-code") String iso2Code) {
        List<SwiftData> swiftDataByIso2CodeList = swiftService.getSwiftDataByIso2Code(iso2Code);

        if(swiftDataByIso2CodeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<SwiftDataDTO> swiftDataDTOList = swiftDataByIso2CodeList.stream()
                .map(data -> {
                    data.setCountryName(null);
                    return DTOMappers.mapToSwiftDataDTO(data, null);
                })
                .collect(Collectors.toList());


        return ResponseEntity.ok(swiftService.findAll().toString());
    }



}
