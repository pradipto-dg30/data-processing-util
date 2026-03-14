package org.assignment.util;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class TestDataGenerator {
    public static void main(String[] args) throws Exception {
        Random rand = new Random();
        String[] ids = {"USR-01", "DEV-02", "SYS-09", "APP-05", "NET-04"};

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get("testdata2.csv")))) {
            writer.println("id,timestamp,value");

            // 1. Generate 950 base records
            for (int i = 0; i < 950; i++) {
                String id = ids[rand.nextInt(ids.length)];
                long ts = 1710415000000L + rand.nextInt(1000000);
                // ~22% are positive (invalid for your count)
                double val = (i < 222) ? rand.nextDouble() * 100 : -rand.nextDouble() * 100;
                writer.printf("%s,%d,%.2f%n", id, ts, val);
            }

            // 2. Generate 50 duplicates (Same ID + Timestamp)
            // Using fixed values so you can easily verify them
            for (int i = 0; i < 50; i++) {
                writer.println("DUP-ID,1710416000000,-10.50");
            }
        }
        System.out.println("File 'testdata2.csv' created with 1,000 records.");
    }
}
