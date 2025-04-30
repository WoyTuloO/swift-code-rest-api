package com.example.remilty.Services;

import com.example.remilty.Models.SwiftData;
import com.example.remilty.Repositories.SwiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public List<SwiftData> getSwiftDataBySwiftCode(String swiftCode) {
        if(swiftCode.endsWith("XXX")){
            List<SwiftData> swiftDataList = swiftRepository.findBySwiftCode(swiftCode);
            if(swiftDataList.isEmpty()){
                new ArrayList<>();
            }

            List<SwiftData> allBySwiftCodeStartingWith = swiftRepository.findAllBySwiftCodeStartingWith(swiftCode.substring(0, swiftCode.length() - 3));

            if(!allBySwiftCodeStartingWith.isEmpty()){
                swiftDataList.addAll(allBySwiftCodeStartingWith);
            }

            return swiftDataList;
        }

        List<SwiftData> swiftData = swiftRepository.findBySwiftCode(swiftCode);
        if(!swiftData.isEmpty()){
            return swiftData;
        }
        return new ArrayList<>();
    }

    public List<SwiftData> getSwiftDataByIso2Code(String iso2Code) {
        return swiftRepository.findByCountryISO2(iso2Code);
    }


    public List<SwiftData> findAll() {
        return swiftRepository.findAll();
    }
}

