package org.assignment.util;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventProcessingUtility {
    public static Map<String, Map<String, Object>> getAggrStats(Stream<Event> recordStream) {

        //Set added for filter with combination of id+timestamp
        Set<String> seenKeys = new HashSet<>();

        return recordStream
//                .parallel()
                .filter(e -> seenKeys.add(e.id()+"_"+e.timestamp()))
                .collect(Collectors.groupingBy(
                Event::id,
                Collector.of(
                        StatsAccumulator::new,
                        StatsAccumulator::accept,
                        StatsAccumulator::combine,
                        StatsAccumulator::toMap
                )
        ));
    }

    private static class StatsAccumulator {
        private long validCount = 0;
        private long minTimestamp = Long.MAX_VALUE;
        private long maxTimestamp = Long.MIN_VALUE;
        private double sumValue = 0;
        private long totalCount = 0;

        void accept(Event e) {
            if (e.value() < 0) validCount++;
            minTimestamp = Math.min(minTimestamp, e.timestamp());
            maxTimestamp = Math.max(maxTimestamp, e.timestamp());
            sumValue += e.value();
            totalCount++;
        }

        StatsAccumulator combine(StatsAccumulator other) {
            this.validCount += other.validCount;
            this.minTimestamp = Math.min(this.minTimestamp, other.minTimestamp);
            this.maxTimestamp = Math.max(maxTimestamp, other.maxTimestamp);
            this.sumValue += other.sumValue;
            this.totalCount += other.totalCount;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> result = new HashMap<>();
            result.put("validEventCount", validCount);
            result.put("minTimestamp", minTimestamp == Long.MAX_VALUE ? null : minTimestamp);
            result.put("maxTimestamp", maxTimestamp == Long.MIN_VALUE ? null : maxTimestamp);
            result.put("averageValue", totalCount == 0 ? 0.0 : sumValue / totalCount);
            return result;
        }
    }

}
