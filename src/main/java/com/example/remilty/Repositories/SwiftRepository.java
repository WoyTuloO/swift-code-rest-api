package com.example.remilty.Repositories;

import com.example.remilty.Models.SwiftData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwiftRepository extends JpaRepository<SwiftData, Long> {
    List<SwiftData> findBySwiftCode(String swiftCode);
    List<SwiftData> findByCountryISO2(String countryISO2);
    List<SwiftData> findAllBySwiftCodeStartingWith(String swiftCodePrefix);
    boolean existsBySwiftCode(String swiftCode);
}
