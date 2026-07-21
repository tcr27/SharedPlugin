package dev.shared.tcr27.tasks;

import com.github.manolo8.darkbot.core.manager.GuiManager;
import com.github.manolo8.darkbot.core.objects.Gui;
import com.github.manolo8.darkbot.core.objects.gui.AssemblyGui;
import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.extensions.Task;
import eu.darkbot.api.managers.AssemblyAPI;

@Feature(name = "Assembly Manager", description = "Something")
public class AssemblyHelper implements Task {

    private final GuiManager xyz;

    private long nextCheck = 0L;
    private int index = 0;

    public AssemblyHelper(PluginAPI api) {
        this.xyz = api.requireInstance(GuiManager.class);

    }

    @Override
    public void onTickTask() {
        if (System.currentTimeMillis() < nextCheck) {
            return;
        }

        Gui quests = this.xyz.quests;
        if (quests != null) {
            System.out.println("x: " + quests.getX() + ", y: " + quests.getY());
        }
        Gui ass = this.xyz.assembly;
        if (ass != null && ass.isVisible()) {
            if (index > 6) {
                index = 0;
                ass.show(false);
            }
            System.out.println("x: " + ass.getX() + ", y: " + ass.getY());
            ass.click(67 + (33 * index), 110);
            System.out.println("index: " + index);
            index++;
        }
        this.nextCheck = System.currentTimeMillis() + 2_000L;
    }
}
