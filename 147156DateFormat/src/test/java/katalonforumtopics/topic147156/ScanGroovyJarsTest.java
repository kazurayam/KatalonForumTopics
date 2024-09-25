package katalonforumtopics.topic147156;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ScanGroovyJarsTest {

    private static final Logger logger = LoggerFactory.getLogger(ScanGroovyJarsTest.class);

    private Path libDir = Paths.get(
            "/Applications/Katalon Studio.app/Contents/Eclipse/configuration/resources/lib");

    @Test
    public void testScanGroovyJars() throws IOException {
        logger.debug("libDir: {}", libDir.toString());
        assertThat(libDir).exists();
        try (Stream<Path> stream = Files.list(libDir)) {
            List<Path> groovyJars = stream
                    .filter(p -> ! Files.isDirectory(p))
                    .filter(p -> p.getFileName().toString().startsWith("groovy-"))
                    .toList();
            assertThat(groovyJars.size()).isGreaterThan(0);
            for (Path groovyJar : groovyJars) {
                // logger.debug("groovyJar: {}", groovyJar);
                try (ZipFile zipFile = new ZipFile(groovyJar.toFile())) {
                    Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        if (entry.getName().contains("org.codehaus.groovy.runtime.ExtensionModule")) {
                            logger.debug("{} contains {}", groovyJar.getFileName().toString(), entry.getName());
                        }
                    }
                }
            }
        }
    }
}
