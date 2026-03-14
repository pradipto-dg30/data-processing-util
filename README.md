# Event Processing Utility

A high-performance Java utility for streaming telemetry data aggregation with a strict **zero-materialization** constraint.

## 🚀 How to Build and Run Tests in IntelliJ

1. **Open Project**: Select `File > Open` and navigate to the project root directory.
2. **Enable Annotation Processing**: This project uses **Lombok**. You must enable annotation processing for the code to compile correctly.
   
3. **Build**: Since this is a maven project, use `mvn clean install` to build the JAR file.
4. **Run Tests**:
   - Locate your test class in the `src/test/java` directory (e.g., `EventProcessingUtilityTest`).
   - Right-click the class file and select **Run 'EventProcessingUtilityTest'**.

---

##  Key Design Decisions

*   Instead of storing event objects, we use a custom `StatsAccumulator`. This acts as custom collector for the Stream pipeline and only stores primitive types (`long`, `double`), ensuring memory overhead remains constant per unique ID regardless of total event volume.
*   The implementation utilizes `.parallel()` for more efficient data aggregation, than sequential.
*   A `ConcurrentHashMap.newKeySet()` is used as a stateful filter. This ensures that when multiple threads process data simultaneously, the deduplication check (`id + timestamp`) remains accurate and thread-safe.
*   Included Project Lombok and SLF4J dependencies for efficient logging.

---

## Assumptions

1. I assumed that the return type of the utility response is of type `Map<String, Map<String, Object>`.
2. Java version is 21.
3. Input for the utility is of type `Stream<Event>`.