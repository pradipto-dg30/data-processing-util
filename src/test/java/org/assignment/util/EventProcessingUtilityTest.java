package org.assignment.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class EventProcessingUtilityTest {

    @Test
    void testRegularScenario() throws IOException {

        //CSV file containing 1000 records, including invalids and duplicates
        Stream<Event> input = loadEventsFromFile("src/test/resources/testdata.csv");
        //start
        long startTime = System.currentTimeMillis();
        //code
        Map<String, Map<String, Object>> res = EventProcessingUtility.getAggrStats(input);
        //end
        long endTime = System.currentTimeMillis();

        log.info("Execution time for 1000 records: " + (endTime - startTime) + "ms");

        assertNotNull(res.get("NET-04"));
    }

    @Test
    void testEmptyStream() throws IOException {

        Stream<Event> input = Stream.of();

        //start
        long startTime = System.currentTimeMillis();

        //code
        Map<String, Map<String, Object>> res = EventProcessingUtility.getAggrStats(input);

        //end
        long endTime = System.currentTimeMillis();

        log.info("Execution time for empty stream: " + (endTime - startTime) + "ms");

    }

    @Test
    void testSingleImport() throws IOException {

        Stream<Event> input = Stream.of(new Event("ID-05",1710415830074L,0.19));

        //start
        long startTime = System.currentTimeMillis();

        //code
        Map<String, Map<String, Object>> res = EventProcessingUtility.getAggrStats(input);
        //end
        long endTime = System.currentTimeMillis();

        log.info("Execution time for single record: " + (endTime - startTime) + "ms");
        assertNotNull(res.get("ID-05"));
    }

    @Test
    void testMillionEventsPerformance() {

        //Simulation of 1 million records
        Stream<Event> largeStream = IntStream.range(0, 1_000_000)
                .mapToObj(i -> new Event(
                        "ID-" + (i % 10),              // Cycle through 10 unique IDs
                        1710000000000L + i,            // Sequential timestamps
                        (i % 5 == 0) ? 10.0 : -10.0    // 20% positive (invalid for your count)
                ));


        long start = System.currentTimeMillis();
        var results = EventProcessingUtility.getAggrStats(largeStream);
        long end = System.currentTimeMillis();

        log.info("Execution time 1M records: " + (end - start) + "ms");

        assertNotNull(results.get("ID-0"));
    }

    public Stream<Event> loadEventsFromFile(String path) throws IOException {
        // Files.lines must be closed by the caller!
        return Files.lines(Paths.get(path))
                .skip(1)
                .map(line -> {
                    try {
                        String[] parts = line.split(",");
                        // Use trim() to avoid NumberFormatException from leading/trailing spaces
                        return new Event(
                                parts[0],
                                Long.parseLong(parts[1].trim()),
                                Double.parseDouble(parts[2].trim())
                        );
                    } catch (Exception e) {
                        log.error("Skipping malformed line: " + line);
                        return null;
                    }
                })
                .filter(Objects::nonNull); // Remove the failed lines from the stream
    }
}