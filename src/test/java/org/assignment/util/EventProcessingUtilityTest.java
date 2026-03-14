package org.assignment.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

class EventProcessingUtilityTest {

    @Test
    void testRegularScenario() throws IOException {

        Stream<Event> input = loadEventsFromFile("src/test/resources/testdata2.csv");

        //start
        long startTime = System.nanoTime();

        //code
        Map<String, Map<String, Object>> res = EventProcessingUtility.getAggrStats(input);

        //end
        long endTime = System.nanoTime();

        //nano
        long durationNano = endTime - startTime;

        //milli
        long durationMillis = durationNano / 1000000;

        System.out.println("Execution time in nanoseconds: " + durationNano);
        System.out.println("Execution time in milliseconds: " + durationMillis);


        System.out.println(res);
    }

    public Stream<Event> loadEventsFromFile(String path) throws IOException {
        return Files.lines(Paths.get(path))
                .skip(1) // skip CSV header
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Event(parts[0], Long.parseLong(parts[1]), Double.parseDouble(parts[2]));
                });
    }
}