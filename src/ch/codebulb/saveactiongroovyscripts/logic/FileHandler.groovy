package ch.codebulb.saveactiongroovyscripts.logic

import ch.codebulb.saveactiongroovyscripts.model.CodeFile
import ch.codebulb.saveactiongroovyscripts.plugin.Settings
import ch.codebulb.saveactiongroovyscripts.util.CodeLine
import ch.codebulb.saveactiongroovyscripts.util.PropertiesFiles
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import groovy.transform.CompileStatic

@CompileStatic
class FileHandler {
    private static final Map<String, String> GROOVY_SCRIPTS_PER_FILE_PATH

    static {
        Settings settings = ServiceManager.getService(Settings.class);
        // fix Windows filepath issues
        String userHome = System.getProperty('user.home').replaceAll('\\\\', '/')
        String path = settings.groovyScriptConfigFilePath.replace('$userHome', userHome)
        GROOVY_SCRIPTS_PER_FILE_PATH = PropertiesFiles.read(new File(path).text)
                .findAll {it instanceof CodeLine.Text}
                .collect {it as CodeLine.Text}
                .collectEntries {[(it.key): it.cdata]}
    }

    public static void process(Project project, PsiFile psiFile) {
        process(new CodeFile(psiFile.virtualFile), new CodeFile(project.baseDir))
    }

    public static void process(Map<String, String> groovyScriptsPerFilePath=GROOVY_SCRIPTS_PER_FILE_PATH, CodeFile file, CodeFile projectBaseDir) {
        String content = file.text
        GROOVY_SCRIPTS_PER_FILE_PATH.each { key, value ->
            if (file.path ==~ key) {
                content = executeGroovyScript(value, content, file, projectBaseDir)
            }
        }
        if (content != null) {
            file.text = content
            file.save()
        }
    }

    private static String executeGroovyScript(String scriptPath, String content, CodeFile file, CodeFile projectBaseDir) {
        Binding binding = new Binding()
        binding.setVariable('content', content)
        binding.setVariable('file', file)
        binding.setVariable('projectBaseDir', projectBaseDir)
        return new GroovyShell(binding).evaluate(new File(scriptPath))
    }
}
