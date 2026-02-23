package analyzer;

import java.util.*;

public class CoverageSummaryAnalyzer {

    public static class Counter {
        public int missed;
        public int covered;

        public Counter() {}
        public Counter(int missed, int covered) { this.missed = missed; this.covered = covered; }

        public int total() { return missed + covered; }
        public double pct() { return total() == 0 ? 100.0 : (covered * 100.0) / total(); }
    }

    public static class ClassCoverage {
        public String packageName;   // e.g., com.example.service
        public String className;     // e.g., BookingService
        public String fqcn;          // e.g., com.example.service.BookingService
        public String sourceFile;    // e.g., BookingService.java
        public Map<String, Counter> counters = new LinkedHashMap<>();

        public ClassCoverage(String pkg, String cls, String sourceFile) {
            this.packageName = pkg;
            this.className = cls;
            this.fqcn = (pkg == null || pkg.isEmpty()) ? cls : (pkg + "." + cls);
            this.sourceFile = sourceFile;
        }

        public Counter get(String type) { return counters.computeIfAbsent(type, t -> new Counter()); }

        public double pct(String type) {
            Counter c = counters.get(type);
            return c == null ? 100.0 : c.pct();
        }
    }

    public static class PackageCoverage {
        public String packageName;
        // type -> counter (LINE, BRANCH, COMPLEXITY, INSTRUCTION, METHOD, CLASS)
        public Map<String, Counter> counters = new LinkedHashMap<>();
        public List<ClassCoverage> classes = new ArrayList<>();

        public PackageCoverage(String name) { this.packageName = name; }
        public Counter get(String type) { return counters.computeIfAbsent(type, t -> new Counter()); }
    }

    public List<PackageCoverage> packages = new ArrayList<>();
    public int totalLineMissed = 0;
    public int totalLineCovered = 0;

    public double totalLinePct() {
        int total = totalLineMissed + totalLineCovered;
        return total == 0 ? 100.0 : (totalLineCovered * 100.0) / total;
    }
}
