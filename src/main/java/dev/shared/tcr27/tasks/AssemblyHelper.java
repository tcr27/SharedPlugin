package dev.shared.tcr27.tasks;

import com.github.manolo8.darkbot.core.manager.GuiManager;
import com.github.manolo8.darkbot.core.objects.gui.AssemblyGui;
import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.extensions.Task;
import eu.darkbot.api.managers.AssemblyAPI;

@Feature(name = "Assembly Manager", description = "Something")
public class AssemblyHelper implements Task {

    private final GuiManager xyz;
    private long nextCheck = 0L;

    public AssemblyHelper(PluginAPI api) {
        this.xyz = api.requireInstance(GuiManager.class);
    }

    @Override
    public void onTickTask() {
        if (System.currentTimeMillis() < nextCheck) {
            return;
        }
        this.xyz.assembly.show(true);
        this.nextCheck = System.currentTimeMillis() +  15_000L;
    }
}
