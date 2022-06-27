package part1;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertiesParserTest {

    @Test
    public void applyPattern_shouldReplaceFoo() throws IOException {
        Properties props = load("my.properties");
        PropertiesParser parser = new PropertiesParser(props);
        assertEquals("foo is fine", parser.applyPattern("${FOO} is fine"));
        assertEquals("foo", parser.applyPattern("${FOO}"));
        assertEquals("foo is foo and nothing else", parser.applyPattern("${FOO} is ${FOO} and nothing else"));
    }

    @Test
    public void applyPattern_shouldNotReplace_unknownKey() throws IOException {
        Properties props = load("my.properties");
        PropertiesParser parser = new PropertiesParser(props);
        assertEquals("${abcdefgh} should not be replaced", parser.applyPattern("${abcdefgh} should not be replaced"));
    }

    @Test
    public void applyPattern_shouldNotReplaceFoo_caseSensitive() throws IOException {
        Properties props = load("my.properties");
        PropertiesParser parser = new PropertiesParser(props);
        assertEquals("${foo} is fine", parser.applyPattern("${foo} is fine"));
    }

    @Test
    public void applyPattern_shouldReplaceFooAndBar() throws IOException {
        Properties props = load("my.properties");
        PropertiesParser parser = new PropertiesParser(props);
        assertEquals("foo is bar", parser.applyPattern("${FOO} is ${BAR}"));
    }

    @Test
    public void applyPattern_shouldReplaceBaz() throws IOException {
        Properties props = load("my.properties");
        PropertiesParser parser = new PropertiesParser(props);
        assertEquals("foo is bar", parser.applyPattern("${baz}"));
    }

    @Test
    public void applyPattern_shouldThrowIllegalArgumentException_infiniteRecursion() throws IOException {
        Properties props = load("infinite_recursion.properties");
        PropertiesParser parser = new PropertiesParser(props);
        assertThrowsExactly(IllegalArgumentException.class, () -> parser.applyPattern("${GNU}'s Not Unix"));
    }

    @Test
    public void parse_shouldParseBaz() throws IOException {
        Properties props = load("my.properties");
        PropertiesParser parser = new PropertiesParser(props);
        parser.parse();
        assertEquals("foo is bar", props.getProperty("baz"));
    }

    @Test
    public void parse_shouldParseFoobar() throws IOException {
        Properties props = load("my.properties");
        PropertiesParser parser = new PropertiesParser(props);
        parser.parse();
        assertEquals("foo is bar", props.getProperty("FOOBAR"));
    }

    @Test
    public void parse_shouldParseWaldo_mixUnknownAndKnownKeys() throws IOException {
        Properties props = load("my.properties");
        PropertiesParser parser = new PropertiesParser(props);
        parser.parse();
        assertEquals("${ABCD} and bar", props.getProperty("WALDO"));
    }

    @Test
    public void parse_shouldThrowIllegalArgumentException_infiniteRecursion() throws IOException {
        Properties props = load("infinite_recursion.properties");
        PropertiesParser parser = new PropertiesParser(props);
        assertThrowsExactly(IllegalArgumentException.class, () -> parser.parse());
    }

    private Properties load(String file) throws IOException {
        Properties props = new Properties();
        try (InputStream input = PropertiesParserTest.class.getClassLoader().getResourceAsStream(file)) {
            props.load(input);
        }
        return props;
    }

}
