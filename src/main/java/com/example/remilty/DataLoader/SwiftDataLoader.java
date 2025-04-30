package com.example.remilty.DataLoader;

import com.example.remilty.Models.SwiftData;
import com.example.remilty.Repositories.SwiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class SwiftDataLoader implements CommandLineRunner {

    private final SwiftRepository swiftRepository;

    @Autowired
    public SwiftDataLoader(SwiftRepository swiftRepository) {
        System.out.println("SwiftDataLoader: ładowanie danych startuje!");
        this.swiftRepository = swiftRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        File file = new File("src/main/resources/db.tsv");
        List<SwiftData> swiftData = loadDbData(file);
        if (!swiftData.isEmpty())
            swiftRepository.saveAll(swiftData);
    }

    public List<SwiftData> loadDbData(File file) {
        List<SwiftData> swiftDataList = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] dataSplit = line.split("\t");

                SwiftData swiftData = new SwiftData(
                        dataSplit[0],
                        dataSplit[1],
                        dataSplit[3],
                        dataSplit[4],
                        dataSplit[6]
                );

                if(!swiftRepository.existsBySwiftCode(swiftData.getSwiftCode()))
                    swiftDataList.add(swiftData);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return swiftDataList;
    }
}
