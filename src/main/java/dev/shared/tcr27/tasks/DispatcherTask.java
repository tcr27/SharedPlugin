package dev.shared.tcr27.tasks;

import com.github.manolo8.darkbot.core.manager.DispatchManager;
import com.github.manolo8.darkbot.utils.Time;
import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.extensions.Task;

@Feature(name = "Dispatcher", description = "Automatically dispatches stuff.")
public class DispatcherTask implements Task {
    private long nextCheck = 0;
    private final DispatchManager dispatchManager;

    public DispatcherTask(PluginAPI api) {
        this.dispatchManager = api.requireInstance(DispatchManager.class);
    }
/*
    TODO:
        - Collect finished retriever
        - Check if any open slots are available
        - Hire retriever
        - Increase check to next due retriever
 */

    @Override
    public void onTickTask() {
        if (this.nextCheck <= System.currentTimeMillis()) {
/*
            this.dispatchManager.openRetrieverTab();
            Time.sleep(500);
            this.dispatchManager.openAvailableTab();
            Time.sleep(500);

            this.dispatchManager.click(650, 300);


            this.dispatchManager.click(300, 150);
            Time.sleep(1000);
            this.dispatchManager.click(300, 200);
            Time.sleep(1000);
            this.dispatchManager.click(300, 250);
            Time.sleep(1000);
            this.dispatchManager.click(300, 260);
            Time.sleep(1000);
            this.dispatchManager.click(300, 300);
            Time.sleep(1000);
            this.dispatchManager.click(300, 350);


            if (this.dispatchManager.getAvailableSlots() == 66) {
                this.dispatchManager.openRetrieverTab();
                Time.sleep(500);
                this.dispatchManager.openAvailableTab();
                Time.sleep(500);
                this.dispatchManager.clickFirstItem();
                Time.sleep(500);
                this.dispatchManager.clickHire();
                Time.sleep(500);
                this.dispatchManager.clickAcceptPopup();
                Time.sleep(500);
            }
            this.dispatchManager.clickCloseOkPopup();
            */
            this.nextCheck = System.currentTimeMillis() + 10_000L;
        }
    }
}
