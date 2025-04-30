package com.example.remilty.DataLoader;

import com.example.remilty.Models.SwiftData;
import com.example.remilty.Repositories.SwiftRepository;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

public class DataLoaderTest {
    @Test
    void shouldLoadData() throws IOException {
        SwiftRepository repo = mock(SwiftRepository.class);
        SwiftDataLoader loader = new SwiftDataLoader(repo);

        File tmp = File.createTempFile("test", ".tsv");

        try(FileWriter writer = new FileWriter(tmp)){
            writer.write("COUNTRY ISO2 CODE\tSWIFT CODE\tCODE TYPE\tNAME\tADDRESS\tTOWN NAME\tCOUNTRY NAME\tTIME ZONE\n");
            writer.write("BG\tADCRBGS1XXX\tBIC11\tADAMANT CAPITAL PARTNERS AD\tJAMES BOURCHIER BLVD 76A HILL TOWER SOFIA, SOFIA, 1421\tSOFIA\tBULGARIA\tEurope/Sofia\n");
            writer.write("UY\tAFAAUYM1XXX\tBIC11\tAFINIDAD A.F.A.P.S.A.\tPLAZA INDEPENDENCIA 743  MONTEVIDEO, MONTEVIDEO, 11000\tMONTEVIDEO\tURUGUAY\tAmerica/Montevideo\n");
            writer.write("MC\tAGRIMCM1XXX\tBIC11\tCREDIT AGRICOLE MONACO (CRCA PROVENCE COTE D'AZUR MONACO)\t23 BOULEVARD PRINCESSE CHARLOTTE  MONACO, MONACO, 98000\tMONACO\tMONACO\tEurope/Monaco\n");
            writer.write("PL\tAIPOPLP1XXX\tBIC11\tSANTANDER CONSUMER BANK SPOLKA AKCYJNA\tSTRZEGOMSKA 42C  WROCLAW, DOLNOSLASKIE, 53-611\tWROCLAW\tPOLAND\tEurope/Warsaw\n");
            writer.write("LV\tAIZKLV22XXX\tBIC11\tABLV BANK, AS IN LIQUIDATION\tMIHAILA TALA STREET 1  RIGA, RIGA, LV-1045\tRIGA\tLATVIA\tEurope/Riga\n");
            writer.write("MT\tAKBKMTMTXXX\tBIC11\tAKBANK T.A.S. (MALTA BRANCH)\tFLOOR 6, PORTOMASO BUSINESS TOWER 01 PORTOMASO PTM - ST. JULIAN'S ST. JULIAN'S, STJ 4011\tST. JULIAN'S\tMALTA\tEurope/Malta\n");
            writer.write("PL\tALBPPLP1BMW\tBIC11\tALIOR BANK SPOLKA AKCYJNA\t WARSZAWA, MAZOWIECKIE\tWARSZAWA\tPOLAND\tEurope/Warsaw\n");
            writer.write("PL\tALBPPLPWXXX\tBIC11\tALIOR BANK SPOLKA AKCYJNA\tLOPUSZANSKA BUSINESS PARK LOPUSZANSKA 38 D WARSZAWA, MAZOWIECKIE, 02-232\tWARSZAWA\tPOLAND\tEurope/Warsaw\n");
        }

        List<SwiftData> result = loader.loadDbData(tmp);

        assertThat(result.get(0).getSwiftCode()).isEqualTo("ADCRBGS1XXX");
        assertThat(result.get(1).getSwiftCode()).isEqualTo("AFAAUYM1XXX");
        assertThat(result.get(2).getSwiftCode()).isEqualTo("AGRIMCM1XXX");
        assertThat(result.get(3).getSwiftCode()).isEqualTo("AIPOPLP1XXX");
        assertThat(result.get(4).getSwiftCode()).isEqualTo("AIZKLV22XXX");
        assertThat(result.get(5).getSwiftCode()).isEqualTo("AKBKMTMTXXX");
        assertThat(result.get(6).getSwiftCode()).isEqualTo("ALBPPLP1BMW");
        assertThat(result.get(7).getSwiftCode()).isEqualTo("ALBPPLPWXXX");

    }
}
