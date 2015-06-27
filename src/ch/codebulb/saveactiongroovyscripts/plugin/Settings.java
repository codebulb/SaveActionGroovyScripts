package ch.codebulb.saveactiongroovyscripts.plugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;

@State(name = "SaveActionGroovyScriptsSettings",
        storages = {
                @Storage(file = StoragePathMacros.APP_CONFIG + "/saveactionGroovyScripts_settings.xml")})
public class Settings implements PersistentStateComponent<Settings> {

    private boolean activate = true;

    private String groovyScriptConfigFilePath = "$userHome/.IdeaIC14/config/options/onsaveGroovyScriptsConfig.properties";

    public Settings getState() {
        return this;
    }

    public void loadState(Settings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public String getGroovyScriptConfigFilePath() {
        return groovyScriptConfigFilePath;
    }

    public void setGroovyScriptConfigFilePath(String groovyScriptConfigFilePath) {
        this.groovyScriptConfigFilePath = groovyScriptConfigFilePath;
    }
}