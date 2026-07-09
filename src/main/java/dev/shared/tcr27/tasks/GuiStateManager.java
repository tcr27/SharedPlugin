package dev.shared.tcr27.tasks;

import com.github.manolo8.darkbot.core.manager.GuiManager;
import com.github.manolo8.darkbot.core.objects.Gui;
import dev.shared.tcr27.config.GuiStateManagerConfig;
import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.extensions.Configurable;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.extensions.Task;

import java.util.Map;

@Feature(name = "GUI State Manager", description = "Ensures configured GUIs are opened or hidden.")
public class GuiStateManager implements Task, Configurable<GuiStateManagerConfig> {
    private GuiStateManagerConfig config;
    private final GuiManager guiManager;
    private long nextCheck = 0L;

    public GuiStateManager(PluginAPI api) {
        this.guiManager = api.requireInstance(GuiManager.class);
    }

    @Override
    public void setConfig(ConfigSetting<GuiStateManagerConfig> setting) {
        this.config = setting.getValue();
    }

    @Override
    public void onTickTask() {
        if (System.currentTimeMillis() < nextCheck) {
            return;
        }
        for (Map.Entry<String, Boolean> entry : config.guiStates.asMap().entrySet()) {
            setGuiState(entry.getKey(), entry.getValue());
        }
        nextCheck = System.currentTimeMillis() + config.checkInterval * 1000L;
    }

    private void setGuiState(String guiId, boolean visible) {
        Gui gui = guiManager.getGui(guiId);
        if (gui == null) {
            return;
        }
        gui.show(visible);
    }
}
