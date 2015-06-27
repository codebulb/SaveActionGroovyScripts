package ch.codebulb.saveactiongroovyscripts.util

import groovy.transform.CompileStatic
import org.junit.Test

import static ch.codebulb.saveactiongroovyscripts.util.CodeLineTestUtil.assertMatch
import static ch.codebulb.saveactiongroovyscripts.util.CodeLineTestUtil.cap

@CompileStatic
class XmlFilesTest {
    private static final String SOURCE = cap """
<root>
\t<element name="first">one</element>
\t<!-- empty line: -->

\t<element name="second">two</element>
</root>
"""

    private static final List<CodeLine> MODEL = [
            new CodeLine.Text('first', 'one'),
            new CodeLine.Comment('empty line:'),
            new CodeLine.Empty(),
            new CodeLine.Text('second', 'two'),
    ].asImmutable()

    @Test
    void testRead() {
        List<CodeLine> output = XmlFiles.read(SOURCE, 'name')
        assertMatch (MODEL, output)
    }

    @Test
    void testWrite() {
        String output = XmlFiles.write(MODEL, 'root', 'element', 'name')
        assertMatch (SOURCE, output)
    }
}
