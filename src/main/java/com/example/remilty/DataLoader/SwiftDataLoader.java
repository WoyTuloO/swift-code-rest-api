package com.example.remilty.DataLoader;

import com.example.remilty.Models.SwiftData;
import com.example.remilty.Repositories.SwiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!test")
public class SwiftDataLoader implements CommandLineRunner {

    private final SwiftRepository swiftRepository;

    @Autowired
    public SwiftDataLoader(SwiftRepository swiftRepository) {
        this.swiftRepository = swiftRepository;
    }

    @Override
    public void run(String... args) throws IOException {
        try(
            InputStream inputStream = new ClassPathResource("db.tsv").getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ){
            List<SwiftData> swiftData = readBufferedReader(reader);
            if (!swiftData.isEmpty())
                swiftRepository.saveAll(swiftData);
        }
    }


    public List<SwiftData> loadDbData(File file){
        try (
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        ) {
            return readBufferedReader(br);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SwiftData> readBufferedReader(BufferedReader br) throws IOException {
        List<SwiftData> swiftDataList = new ArrayList<>();
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] dataSplit = line.split("\t");

            SwiftData swiftData = new SwiftData(
                    dataSplit[0].trim().toUpperCase(),
                    dataSplit[1],
                    dataSplit[3],
                    dataSplit[4],
                    dataSplit[6].trim().toUpperCase()
            );

            if (!swiftRepository.existsBySwiftCode(swiftData.getSwiftCode()))
                swiftDataList.add(swiftData);

        }

        return swiftDataList;
    }
}
