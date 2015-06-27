package ch.codebulb.saveactiongroovyscripts.plugin.processors;

import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.FutureTask;

public class FileHandlingLayoutCodeProcessor extends AbstractLayoutCodeProcessor {
    private static final String PROGRESS_TEXT = "progress.text.adapter";
    public static final String COMMAND_NAME = "process.adapter";

    private Project project;

    protected FileHandlingLayoutCodeProcessor(Project project, PsiFile file) {
        super(project, file, PROGRESS_TEXT, COMMAND_NAME, false);
        this.project = project;
    }

    @NotNull
    @Override
    protected FutureTask<Boolean> prepareTask(PsiFile file, boolean processChangedTextOnly) throws IncorrectOperationException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SynchronizedFileHandlingLayoutCodeProcessor.process(project, file);
            }
        };
        return new FutureTask<Boolean>(runnable, true);
    }
}
