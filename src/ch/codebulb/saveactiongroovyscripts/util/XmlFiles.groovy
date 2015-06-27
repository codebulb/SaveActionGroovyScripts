package ch.codebulb.saveactiongroovyscripts.util

import groovy.transform.CompileStatic
import groovy.util.slurpersupport.GPathResult
/**
 * A simple helper for reading / writing very simple, 1-level key/value holding xml files, as e.g. used by
 * Android SDK's res/values/strings.xml files.
 */
@CompileStatic
class XmlFiles {
    private XmlFiles() {}

    /**
     * Parses the XML input and returns it as a List of {@link CodeLine}s.
     * @param input the input provided
     * @param attributeName the name of the xml element node's attribute which holds the "key" information
     * @return the parsed input
     */
    public static List<CodeLine> read(String input, String attributeName) {
        // accept inline comments as well
        input = input.replaceAll('<!--(.*)-->') { all, String cdata ->
            $/<_comment $attributeName="--comment--">${cdata.trim()}</_comment>/$
        }

        input = input.split('\n').collect { String line ->
            if (line.trim() == '') {
                """<_empty $attributeName="--empty--"/>"""
            }
            else {
                line
            }
        }.join('\n')

        // now we have a proper xml file
        GPathResult root = new XmlSlurper().parseText(input)
        return root.children().collect() { GPathResult child ->
            if (child.getProperty("@$attributeName") == '--empty--') {
                return new CodeLine.Empty()
            }
            else if (child.getProperty("@$attributeName") == '--comment--') {
                return new CodeLine.Comment(child.text())
            }
            else {
                return new CodeLine.Text(child.getProperty("@$attributeName").toString(), child.text())
            }
        }
    }

    /**
     * Converts the input List of {@link CodeLine}s into their XML representation.
     * @param codeLines the code lines provided
     * @param rootElement the desired name of the xml root element
     * @param elementName the desired name of the xml element node
     * @param attributeName the desired name of the xml element node's attribute which holds the "key" information
     * @return the XML representation
     */
    public static String write(List<? extends CodeLine> codeLines,
                               String rootElement, String elementName, String attributeName) {
        // Note: IntelliJ shows error, but it compiles perfectly.
        return "<$rootElement>\n" +
            codeLines*.toXmlLine(elementName, attributeName).join('\n') +
            "\n</$rootElement>"
    }

}