package ch.codebulb.saveactiongroovyscripts.util

import groovy.transform.CompileStatic
import org.junit.Test

import static ch.codebulb.saveactiongroovyscripts.util.CodeLineTestUtil.*

@CompileStatic
class PropertiesFilesTest {
    private static final String SOURCE = cap """
first=one
# empty line:

second = two
"""

    private static final String SOURCE_STRICT = cap """
first=one
# empty line:

second=two
"""

    private static final List<CodeLine> MODEL = [
            new CodeLine.Text('first', 'one'),
            new CodeLine.Comment('empty line:'),
            new CodeLine.Empty(),
            new CodeLine.Text('second', 'two'),
    ].asImmutable()

    @Test
    void testRead() {
        List<CodeLine> output = PropertiesFiles.read(SOURCE)
        assertMatch (MODEL, output)
    }

    @Test
    void testWrite() {
        String output = PropertiesFiles.write(MODEL)
        assertMatch (SOURCE_STRICT, output)
    }
}
