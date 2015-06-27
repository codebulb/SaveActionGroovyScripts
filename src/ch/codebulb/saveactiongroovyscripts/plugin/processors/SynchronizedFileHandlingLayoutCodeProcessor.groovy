package ch.codebulb.saveactiongroovyscripts.plugin.processors

import ch.codebulb.saveactiongroovyscripts.logic.FileHandler
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import groovy.transform.CompileStatic

@CompileStatic
class SynchronizedFileHandlingLayoutCodeProcessor {
    private static boolean running

    public static void process(Project project, PsiFile file) {
        // Prevents a file.save() call to trigger code processing recursively
        if (running) {
            running = false
            return
        }
        FileHandler.process(project, file);
        running = true
    }
}
