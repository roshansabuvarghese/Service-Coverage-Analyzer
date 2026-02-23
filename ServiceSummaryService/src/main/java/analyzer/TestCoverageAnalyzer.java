package analyzer;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Path;

public class TestCoverageAnalyzer {

    /** Parse JaCoCo XML and return a structured summary (package + class level). */
    public static CoverageSummaryAnalyzer parse(Path jacocoXmlPath) {
        CoverageSummaryAnalyzer summary = new CoverageSummaryAnalyzer();
        try {
            File xmlFile = jacocoXmlPath.toFile();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            // keep parsing simple/fast and safe
            dbFactory.setNamespaceAware(false);
            dbFactory.setValidating(false);
            dbFactory.setXIncludeAware(false);

            // 🚫 Do not fetch external DTDs / schemas
            try { dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); } catch (Exception ignored) {}
            try { dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); } catch (Exception ignored) {}
            try { dbFactory.setFeature("http://xml.org/sax/features/external-general-entities", false); } catch (Exception ignored) {}
            try { dbFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false); } catch (Exception ignored) {}
            try { dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); } catch (Exception ignored) {}
            try { dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); } catch (Exception ignored) {}

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            // If a DOCTYPE is present, return an empty DTD so no file is read.
            dBuilder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));

            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList pkgNodes = doc.getElementsByTagName("package");
            for (int i = 0; i < pkgNodes.getLength(); i++) {
                Element pkg = (Element) pkgNodes.item(i);
                String pkgNameInternal = pkg.getAttribute("name");                 // e.g., com/example/service
                String pkgName = pkgNameInternal.replace('/', '.');                // e.g., com.example.service

                CoverageSummaryAnalyzer.PackageCoverage pc = new CoverageSummaryAnalyzer.PackageCoverage(pkgName);

                // Direct child counters at package level
                NodeList pkgChildren = pkg.getChildNodes();
                for (int j = 0; j < pkgChildren.getLength(); j++) {
                    Node n = pkgChildren.item(j);
                    if (n.getNodeType() == Node.ELEMENT_NODE && "counter".equals(n.getNodeName())) {
                        Element c = (Element) n;
                        String type = c.getAttribute("type");
                        int missed = intAttr(c, "missed");
                        int covered = intAttr(c, "covered");
                        CoverageSummaryAnalyzer.Counter ctr = pc.get(type);
                        ctr.missed += missed;
                        ctr.covered += covered;

                        if ("LINE".equals(type)) {
                            summary.totalLineMissed += missed;
                            summary.totalLineCovered += covered;
                        }
                    }
                }

                // Class-level coverage
                NodeList classNodes = pkg.getElementsByTagName("class");
                for (int k = 0; k < classNodes.getLength(); k++) {
                    Element cls = (Element) classNodes.item(k);
                    String classNameSimple = cls.getAttribute("name").replace('/', '.');
                    String sourceFile = cls.getAttribute("sourcefilename");

                    String simple = classNameSimple.contains(".")
                            ? classNameSimple.substring(classNameSimple.lastIndexOf('.') + 1)
                            : classNameSimple;
                    simple = simple.contains("$") ? simple.substring(0, simple.indexOf('$')) : simple;

                    CoverageSummaryAnalyzer.ClassCoverage cc = new CoverageSummaryAnalyzer.ClassCoverage(pkgName, simple, sourceFile);

                    NodeList classChildren = cls.getChildNodes();
                    for (int m = 0; m < classChildren.getLength(); m++) {
                        Node cn = classChildren.item(m);
                        if (cn.getNodeType() == Node.ELEMENT_NODE && "counter".equals(cn.getNodeName())) {
                            Element c = (Element) cn;
                            String type = c.getAttribute("type");
                            int missed = intAttr(c, "missed");
                            int covered = intAttr(c, "covered");
                            CoverageSummaryAnalyzer.Counter ctr = cc.get(type);
                            ctr.missed += missed;
                            ctr.covered += covered;
                        }
                    }
                    pc.classes.add(cc);
                }

                summary.packages.add(pc);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JaCoCo XML: " + e.getMessage(), e);
        }
        return summary;
    }

    private static int intAttr(Element el, String name) {
        String v = el.getAttribute(name);
        return (v == null || v.isEmpty()) ? 0 : Integer.parseInt(v);
    }

    /** Optional console print */
    public static void analyze(Path jacocoXmlPath) {
        CoverageSummaryAnalyzer s = parse(jacocoXmlPath);
        System.out.printf("🧮 Total Line Coverage: %.1f%% (%d covered, %d missed)%n",
                s.totalLinePct(), s.totalLineCovered, s.totalLineMissed);
        s.packages.forEach(pc -> {
            System.out.println("- " + pc.packageName);
            pc.counters.forEach((type, ctr) ->
                    System.out.printf("    %s: %.1f%% (%d missed, %d covered)%n",
                            type, ctr.pct(), ctr.missed, ctr.covered));
        });
    }
}
