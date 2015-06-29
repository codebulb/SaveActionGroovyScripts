package ch.codebulb.saveactiongroovyscripts.util

import groovy.transform.CompileStatic

/**
 * A simple helper for reading / writing .properties files.
 */
@CompileStatic
class PropertiesFiles {
    private PropertiesFiles() {}

    /**
     * Parses the Properties file input and returns it as a List of {@link CodeLine}s.
     * @param input the input provided
     */
    public static List<CodeLine> read(String input) {
        return input.split('\n').collect { String line ->
            if (line.trim() == '') {
                return new CodeLine.Empty()
            }
            else if (line ==~ /#.*/) {
                // comment
                return new CodeLine.Comment(line.replace('#', '').trim())
            } else {
                // value
                String[] split = line.split('=')
                // Note: split[1] raises ArrayIndexOutOfBoundsException on empty right-hand side
                return new CodeLine.Text(split[0].trim(), split.size() > 1 ? split[1].trim() : '')
            }
        }
    }

    /**
     * Converts the input List of {@link CodeLine}s into their Properties file representation.
     * @param codeLines the code lines provided
     * @return the Properties file representation
     */
    public static String write(List<? extends CodeLine> codeLines) {
        // Note: IntelliJ shows error, but it compiles perfectly.
        return codeLines*.toPropertyLine().join('\n')
    }
}
