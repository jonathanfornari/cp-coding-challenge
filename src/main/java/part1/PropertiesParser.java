package part1;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * <h3>PropertiesParser</h3>
 *
 * Resolves key substitution on a properties set; in other words, given:
 * <pre>FOO = foo
 * BAR = bar
 * baz = ${FOO} is ${BAR}
 * </pre>
 *
 * the following code will satisfy the assertion:
 * <pre> Properties props = Properties.load('my.properties');
 *  PropertiesParser parser = new PropertiesParser(props);
 *  parser.parse();
 *  assert("foo is bar".equals(props.getProperty("baz"));
 * </pre>
 */
public class PropertiesParser {


    /** A RegEx pattern for a key subst: ${key} */
    private final String PATTERN = "\\$\\{(.*?)\\}";
    private final Properties props;


    public PropertiesParser(Properties props) {
        this.props = props;
    }

    public void parse() {
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            props.put(key, applyPattern(value));
        }
    }


    /**
     * Executes string substitution for the {@code value}
     *
     * @param value
     * @return the fully substituted value, if possible
     */
    protected String applyPattern(String value) {
        Pattern keyPattern = Pattern.compile(PATTERN);
        Matcher m = keyPattern.matcher(value);
        Set<MatchResult> matches = m.results().collect(Collectors.toSet());
        if(matches.size() > 0) {
            for (MatchResult match : matches) {
                String subs = extractKeyValue(match.group(1));
                if (subs != null) {
                    if(subs.contains(match.group())) {
                        throw new IllegalArgumentException(String.format("Infinite recursion detected for subs key '%s' with value '%s'", match.group(), subs));
                    }
                    value = value.replaceAll(Pattern.quote(match.group()), Matcher.quoteReplacement(subs));
                    return applyPattern(value);
                }
            }
        }
        return value;
    }


    /** Replaces the subst key with its value, if available in {@code props}
     *
     * key param should be the capturing group (the dynamic portion of the captured pattern).
     *
     * eg. for ${FOO} key should be FOO. This can be achieved retrieving the capture group with index 1.
     *
     * @see MatchResult#group(int group)
     */
    private String extractKeyValue(String key) {
        return props.getProperty(key);
    }

    // removed this method, no need to parse or find matches two separate times, applyPattern already checks if matches are found for the key subst pattern
    //    private boolean hasSubstKey(String value) {
    //    }
}
