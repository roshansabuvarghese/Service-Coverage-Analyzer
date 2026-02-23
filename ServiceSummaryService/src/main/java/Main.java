import analyzer.CoverageSummaryAnalyzer;
import analyzer.TestCoverageAnalyzer;
import reporter.MarkdownReporter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java -cp <jar> extractor.Main <path-to-service-root>");
            return;
        }

        Path serviceRoot = Paths.get(args[0]).toAbsolutePath().normalize();
        if (!Files.isDirectory(serviceRoot)) {
            System.out.println("Error: Provided path is not a directory: " + serviceRoot);
            return;
        }

        System.out.println("Analyzing: " + serviceRoot);

        Path report = findCoverageReport(serviceRoot);
        if (report == null) {
            System.out.println("No JaCoCo XML found. Attempting to generate coverage report...");
            boolean generated = generateCoverageReport(serviceRoot);
            if (generated) report = findCoverageReport(serviceRoot);
        }

        if (report != null && Files.exists(report)) {
            System.out.println("Found coverage report: " + report);
            CoverageSummaryAnalyzer summary = TestCoverageAnalyzer.parse(report);

            // Optional: print quick console summary
            System.out.printf("🧮 Total Line Coverage: %.1f%% (%d covered / %d missed)%n",
                    summary.totalLinePct(), summary.totalLineCovered, summary.totalLineMissed);

            // Write Markdown file
            Path mdOut = Paths.get("test-coverage-report.md");
            MarkdownReporter.write(summary, mdOut);
        } else {
            System.out.println("❌ Could not find or generate a JaCoCo XML report.");
        }
    }

    // --- same helper methods as before ---

    private static Path findCoverageReport(Path root) {
        String[] candidates = new String[] {
                "target/site/jacoco/jacoco.xml",
                "target/site/jacoco-aggregate/jacoco.xml",
                "build/reports/jacoco/test/jacocoTestReport.xml",
                "target/jacoco-report/jacoco.xml"
        };
        for (String rel : candidates) {
            Path p = root.resolve(rel);
            if (Files.exists(p)) return p;
        }
        return null;
    }

    private static boolean generateCoverageReport(Path root) {
        try {
            boolean isMaven = Files.exists(root.resolve("pom.xml")) || Files.exists(root.resolve("mvnw")) || Files.exists(root.resolve("mvnw.cmd"));
            boolean isGradle = Files.exists(root.resolve("build.gradle")) || Files.exists(root.resolve("build.gradle.kts"))
                    || Files.exists(root.resolve("gradlew")) || Files.exists(root.resolve("gradlew.bat"));

            if (isMaven) return runMavenCoverage(root);
            if (isGradle) return runGradleCoverage(root);

            System.out.println("No Maven/Gradle build files detected. Skipping auto-generation.");
            return false;
        } catch (Exception e) {
            System.out.println("Failed to generate coverage: " + e.getMessage());
            return false;
        }
    }

    private static boolean runMavenCoverage(Path root) throws Exception {
        List<String> cmd = new ArrayList<>();
        Path mvnw = root.resolve(isWindows() ? "mvnw.cmd" : "mvnw");
        cmd.add(Files.exists(mvnw) ? mvnw.toAbsolutePath().toString() : "mvn");
        cmd.add("clean");
        cmd.add("org.jacoco:jacoco-maven-plugin:0.8.11:prepare-agent");
        cmd.add("test");
        cmd.add("org.jacoco:jacoco-maven-plugin:0.8.11:report");
        cmd.add("-DskipTests=false");
        cmd.add("-B");
        cmd.add("-q");
        System.out.println("Running: " + String.join(" ", cmd));
        return exec(cmd, root) == 0;
    }

    private static boolean runGradleCoverage(Path root) throws Exception {
        List<String> cmd = new ArrayList<>();
        Path gradlew = root.resolve(isWindows() ? "gradlew.bat" : "gradlew");
        cmd.add(Files.exists(gradlew) ? gradlew.toAbsolutePath().toString() : "gradle");
        cmd.add("test");
        cmd.add("jacocoTestReport");
        cmd.add("--quiet");
        System.out.println("Running: " + String.join(" ", cmd));
        return exec(cmd, root) == 0;
    }

    private static int exec(List<String> cmd, Path workingDir) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(workingDir.toFile());
        pb.redirectErrorStream(true);
        Process p = pb.start();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) System.out.println(line);
        }
        int code = p.waitFor();
        System.out.println("Process exited with code: " + code);
        return code;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
