package ch.codebulb.saveactiongroovyscripts.util

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

/**
 * A simple model of a key-value code line.
 */
@CompileStatic
abstract class CodeLine {
    /**
     * Returns the Properties file representation of the code line.
     * @return the .properties representation
     */
    abstract String toPropertyLine()
    
    /**
     * Returns the XML file representation of the code line.
     * @param elementName the desired name of the xml element node
     * @param attributeName the desired name of the xml element node's attribute which holds the "key" information
     * @return the .xml representation
     */
    abstract String toXmlLine(String elementName, String attributeName)

    /**
     * A model of an actual code line holding key-value information.
     */
    @TupleConstructor
    @EqualsAndHashCode
    @ToString
    public static class Text extends CodeLine {
        String key
        String cdata

        @Override
        String toPropertyLine() {
            return "$key=$cdata"
        }

        @Override
        String toXmlLine(String elementName, String attributeName) {
            return """\t<$elementName $attributeName="$key">$cdata</$elementName>"""
        }
    }

    /**
     * A model of a comment code line.
     */
    @TupleConstructor
    @EqualsAndHashCode
    @ToString
    public static class Comment extends CodeLine {
        String cdata

        @Override
        String toPropertyLine() {
            return "# $cdata"
        }

        @Override
        String toXmlLine(String elementName, String attributeName) {
            return """\t<!-- $cdata -->"""
        }
    }

    /**
     * A model of an empty code line (i.e. a line break).
     */
    @EqualsAndHashCode
    @ToString
    public static class Empty extends CodeLine {
        @Override
        String toPropertyLine() {
            return ""
        }

        @Override
        String toXmlLine(String elementName, String attributeName) {
            return ""
        }
    }
}
