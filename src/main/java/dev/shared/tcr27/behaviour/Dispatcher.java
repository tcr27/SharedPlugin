package dev.shared.tcr27.behaviour;

import com.github.manolo8.darkbot.core.manager.DispatchManager;
import com.github.manolo8.darkbot.core.manager.GuiManager;
import com.github.manolo8.darkbot.core.objects.Gui;
import com.github.manolo8.darkbot.utils.Time;
import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.extensions.Behavior;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.managers.DispatchAPI;

@Feature(name = "Dispatcher", description = "Hires new Dispatcher.", enabledByDefault = true)
public class Dispatcher implements Behavior {

    private final DispatchManager dispatchManager;
    private long nextCheck = 0L;
    private int i = 0;
    private boolean success = false;

    public Dispatcher(PluginAPI api, DispatchManager dispatchManager) {
        this.dispatchManager = dispatchManager;

    }

    @Override
    public void onTickBehavior() {
        if (this.nextCheck < System.currentTimeMillis()) {

            int availableSlots = this.dispatchManager.getAvailableSlots();
            if (availableSlots == 0) {
                return;
            }
            if (!this.dispatchManager.openRetrieverTab()) {
                return;
            }
            if (!this.dispatchManager.clickFirstItem()) {
                return;
            }
            DispatchAPI.Retriever selectedRetriever = this.dispatchManager.getSelectedRetriever();
            DispatchAPI.Retriever retriever = this.dispatchManager.getAvailableRetrievers()
                    .stream()
                    .filter(r -> r.getName().equals("R-01"))
                    .findFirst()
                    .orElse(null);
            if (retriever == null) {
                System.out.println("Retriever not found");
                return;
            }

            this.dispatchManager.overrideSelectedRetriever(retriever);
            Time.sleep(250);
            System.out.println("Click hire");
            if (!this.dispatchManager.clickHire()) {
                return;
            }
            this.success = true;
            Time.sleep(500);
            System.out.println("click close ok");
            this.dispatchManager.clickCloseOkPopup();
            this.dispatchManager.overrideSelectedRetriever(selectedRetriever);
            this.nextCheck = System.currentTimeMillis() + 10_000L;
        }
    }
}
