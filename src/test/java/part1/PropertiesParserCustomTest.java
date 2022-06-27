package part1;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Runs the customized test cases that can be dynamically included/modified on the project
 */
public class PropertiesParserCustomTest {

    @Test
    public void testAllCustomCases() throws IOException {
        File testResourcesFolder = getResourcesFile("custom-test-cases");
        //skip 1 (parent folder itself)
        Files.walk(testResourcesFolder.toPath(), 1).skip(1).forEach(path -> runCustomCase(path.toFile()));
    }

    private File getResourcesFile(String resourceName) {
        ClassLoader loader = PropertiesParserCustomTest.class.getClassLoader();
        URL url = loader.getResource(resourceName);
        return new File(URLDecoder.decode(url.getPath(), Charset.defaultCharset()));
    }

    private void runCustomCase(File dir) {
        if (dir.isDirectory()) {
            System.out.println("Running custom-test-case: " + dir.getName());
            File inputFile = dir.listFiles((dir1, name) -> name.equalsIgnoreCase("input.properties"))[0];
            File expectedAssertionsFile = dir.listFiles((dir1, name) -> name.equalsIgnoreCase("expected_assertions.properties"))[0];

            try {

                Properties inputProperties = load(inputFile);
                PropertiesParser parser = new PropertiesParser(inputProperties);
                parser.parse();

                Properties expectedOutputAssertions = load(expectedAssertionsFile);
                expectedOutputAssertions.forEach((key, value) ->
                        assertEquals(value, inputProperties.getProperty((String) key), "Test failed for assertion: " + key + " while running custom test: " + dir.getName())
                );
            } catch (IOException e) {
                fail("Unexpected IO Error while running custom-test-case", e);
            }
        }
    }

    private Properties load(File file) throws IOException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(file.getPath())) {
            props.load(input);
        }
        return props;
    }
}
