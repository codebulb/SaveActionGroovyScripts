package ch.codebulb.saveactiongroovyscripts.util

import groovy.transform.CompileStatic
import groovy.transform.PackageScope

@CompileStatic
class CodeLineTestUtil {
    private CodeLineTestUtil() {}

    @PackageScope static void assertMatch(List<CodeLine> expected, List<CodeLine> actual) {
        assert expected.size() == actual.size()
        actual.eachWithIndex { CodeLine codeLine, i ->
            String act = codeLine
            String exp = expected[i]
            assert exp == act
        }
    }

    @PackageScope static void assertMatch(String expected, String actual) {
        assert expected.split('\n').size() == actual.split('\n').size()
        actual.split('\n').eachWithIndex { String line, int i ->
            String exp = expected.split('\n')[i]
            String act = line
            assert exp == act
        }
    }

    @PackageScope static String cap(String input) {
        return input.split('\n')[1..-1].join('\n')
    }
}

