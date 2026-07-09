package dev.shared.tcr27.tasks;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.extensions.Task;
import eu.darkbot.api.game.items.Item;
import eu.darkbot.api.game.items.ItemCategory;
import eu.darkbot.api.game.items.ItemUseResult;
import eu.darkbot.api.game.items.SelectableItem;
import eu.darkbot.api.managers.HeroItemsAPI;

@Feature(name = "Tech Helper", description = "Something", enabledByDefault = true)
public class TechHelper implements Task {

    private final HeroItemsAPI heroItemsApi;

    private long nextCheck = 0L;

    public TechHelper(PluginAPI api) {
        this.heroItemsApi = api.requireAPI(HeroItemsAPI.class);

    }

    @Override
    public void onTickTask() {
        if (nextCheck <= System.currentTimeMillis()) {
            ItemUseResult result = this.heroItemsApi.useItem(SelectableItem.Tech.PRECISION_TARGETER, 5000);
            System.out.println(result.isSuccessful());
            nextCheck = System.currentTimeMillis() + 10_000L;
        }
    }
}
