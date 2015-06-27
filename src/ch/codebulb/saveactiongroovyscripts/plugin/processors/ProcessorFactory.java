package ch.codebulb.saveactiongroovyscripts.plugin.processors;

import ch.codebulb.saveactiongroovyscripts.plugin.Settings;
import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.ArrayList;
import java.util.List;

public enum ProcessorFactory {

    INSTANCE;

    public List<AbstractLayoutCodeProcessor> getSaveActionsProcessors(Project project, PsiFile psiFile, Settings settings) {
        ArrayList<AbstractLayoutCodeProcessor> processors = new ArrayList<AbstractLayoutCodeProcessor>();
        if (settings.isActivate()) {
            processors.add(new FileHandlingLayoutCodeProcessor(project, psiFile));
        }
        return processors;
    }
}
