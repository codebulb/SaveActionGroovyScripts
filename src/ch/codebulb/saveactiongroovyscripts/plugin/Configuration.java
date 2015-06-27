package ch.codebulb.saveactiongroovyscripts.plugin;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Configuration implements Configurable {
    private static final String TEXT_ACTIVATE = "Activate Save action Groovy scripts";

    private static final String TEXT_DISPLAY_NAME = "Save Action Groovy Scripts";

    private Settings settings = ServiceManager.getService(Settings.class);

    private JCheckBox activate;

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel panel = initComponent();
        return panel;
    }

    @Override
    public boolean isModified() {
        boolean modified = settings.isActivate() != activate.isSelected();
        return modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.setActivate(activate.isSelected());
    }

    @Override
    public void reset() {
        activate.setSelected(settings.isActivate());
    }

    @Override
    public void disposeUIResources() {
        activate = null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return TEXT_DISPLAY_NAME;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    private JPanel initComponent() {
        return initPanel();
    }

    private JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(activate = new JCheckBox(TEXT_ACTIVATE));
        return panel;
    }
}
