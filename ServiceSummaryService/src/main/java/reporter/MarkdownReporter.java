package reporter;

import analyzer.CoverageSummaryAnalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

public class MarkdownReporter {

    /** Thresholds (tweak as you like) */
    private static final double LINE_OK = 80.0;
    private static final double BRANCH_OK = 70.0;
    private static final double COMPLEXITY_OK = 70.0;

    /** How many classes to show in Hotspots */
    private static final int HOTSPOTS_TOP_N = 10;

    public static void write(CoverageSummaryAnalyzer s, Path output) throws IOException {
        StringBuilder md = new StringBuilder();

        md.append("# 🧪 Test Coverage Report\n\n");
        md.append("_Generated on ").append(LocalDate.now()).append("_\n\n");

        // Overall
        md.append("## 🔢 Overall Summary\n\n");
        md.append(String.format("- **Line Coverage:** **%.1f%%** (%d covered / %d missed)\n\n",
                s.totalLinePct(), s.totalLineCovered, s.totalLineMissed));

        // Packages
        md.append("## 📦 Coverage by Package\n\n");
        for (CoverageSummaryAnalyzer.PackageCoverage pc : s.packages) {
            md.append("### `").append(pc.packageName).append("`\n\n");
            md.append("| Metric | Coverage | Missed | Covered |\n");
            md.append("|---|---:|---:|---:|\n");
            pc.counters.forEach((type, ctr) ->
                    md.append(String.format("| %s | %.1f%% | %d | %d |\n",
                            type, ctr.pct(), ctr.missed, ctr.covered))
            );
            md.append("\n");
        }

        // Class hotspots
        List<ClassRow> hotspots = collectHotspots(s);
        if (!hotspots.isEmpty()) {
            md.append("## 🚨 Hotspots: Top Classes Needing Tests\n\n");
            md.append("| Class | Line | Branch | Complexity | Missed Lines | Suggested Focus |\n");
            md.append("|---|---:|---:|---:|---:|---|\n");
            for (ClassRow r : hotspots) {
                md.append(String.format("| `%s` | %.1f%% | %.1f%% | %.1f%% | %d | %s |\n",
                        r.fqcn, r.linePct, r.branchPct, r.complexityPct, r.lineMissed, r.focus));
            }
            md.append("\n");
        }

        // Per-class recommendations
        if (!hotspots.isEmpty()) {
            md.append("## 💡 Per-Class Recommendations\n\n");
            for (ClassRow r : hotspots) {
                md.append("### `").append(r.fqcn).append("`\n\n");
                if (r.linePct < LINE_OK) {
                    md.append("- **Line coverage low**: add tests for public methods with business logic; include error paths and null checks. Cover util classes and constructors with logic.\n");
                }
                if (r.branchPct < BRANCH_OK) {
                    md.append("- **Branch coverage low**: write tests for **both true/false** branches of `if`/`else`, each `case` in `switch`, and cover **boundary inputs** (0/1/many, empty collections, nulls). Include exception paths.\n");
                }
                if (r.complexityPct < COMPLEXITY_OK) {
                    md.append("- **Complexity coverage low**: target **long or nested methods**. Add parameterized tests spanning edge cases; ensure loops test 0/1/many iterations; refactor huge methods if feasible to isolate branches.\n");
                }
                md.append("- **Tactics**: use Mockito to isolate dependencies; verify interactions on clients/repositories; include timeouts/retries and negative tests; for controllers, add WebMvc/WebFlux tests; for services, add pure unit tests.\n\n");
            }
        }

        md.append("---\n_End of report_\n");

        Files.writeString(output, md.toString());
        System.out.println("📄 Markdown report written to: " + output.toAbsolutePath());
    }

    // -------- internals --------

    private static List<ClassRow> collectHotspots(CoverageSummaryAnalyzer s) {
        List<ClassRow> rows = new ArrayList<>();
        for (CoverageSummaryAnalyzer.PackageCoverage pc : s.packages) {
            for (CoverageSummaryAnalyzer.ClassCoverage cc : pc.classes) {
                double line = cc.pct("LINE");
                double branch = cc.pct("BRANCH");
                double cx = cc.pct("COMPLEXITY");
                CoverageSummaryAnalyzer.Counter lineCtr = cc.counters.get("LINE");
                int lineMissed = (lineCtr == null) ? 0 : lineCtr.missed;

                boolean needs = (line < LINE_OK) || (branch < BRANCH_OK) || (cx < COMPLEXITY_OK);
                if (!needs) continue;

                String focus = focusFor(line, branch, cx);
                rows.add(new ClassRow(cc.fqcn, line, branch, cx, lineMissed, focus));
            }
        }

        // Sort by branch %, then complexity %, then line %, ascending
        rows.sort(Comparator
                .comparingDouble((ClassRow r) -> r.branchPct)
                .thenComparingDouble(r -> r.complexityPct)
                .thenComparingDouble(r -> r.linePct));

        if (rows.size() > HOTSPOTS_TOP_N) {
            return rows.subList(0, HOTSPOTS_TOP_N);
        }
        return rows;
    }

    private static String focusFor(double line, double branch, double cx) {
        List<String> parts = new ArrayList<>();
        if (branch < BRANCH_OK) parts.add("branches (true/false, switch cases, exceptions)");
        if (cx < COMPLEXITY_OK) parts.add("complex flows (nested logic, loops)");
        if (line < LINE_OK) parts.add("missing lines (core business paths)");
        if (parts.isEmpty()) return "General improvements";
        return String.join(" + ", parts);
    }

    private static class ClassRow {
        String fqcn;
        double linePct;
        double branchPct;
        double complexityPct;
        int lineMissed;
        String focus;

        ClassRow(String fqcn, double linePct, double branchPct, double complexityPct, int lineMissed, String focus) {
            this.fqcn = fqcn;
            this.linePct = round1(linePct);
            this.branchPct = round1(branchPct);
            this.complexityPct = round1(complexityPct);
            this.lineMissed = lineMissed;
            this.focus = focus;
        }

        private static double round1(double v) {
            return Math.round(v * 10.0) / 10.0;
        }
    }
}
