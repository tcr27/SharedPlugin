package dev.shared.tcr27.tasks;

import com.github.manolo8.darkbot.backpage.BackpageManager;
import com.github.manolo8.darkbot.backpage.HangarManager;
import com.github.manolo8.darkbot.backpage.hangar.HangarResponse;
import com.github.manolo8.darkbot.utils.Base64Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.extensions.Task;
import eu.darkbot.api.game.items.Item;
import eu.darkbot.api.game.items.ItemCategory;
import eu.darkbot.api.game.items.ItemUseResult;
import eu.darkbot.api.game.items.SelectableItem;
import eu.darkbot.api.managers.BackpageAPI;
import eu.darkbot.api.managers.HangarAPI;
import eu.darkbot.api.managers.HeroItemsAPI;
import eu.darkbot.api.managers.InventoryAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Feature(name = "Tech Helper", description = "Something", enabledByDefault = true)
public class TechHelper implements Task {

    private final HeroItemsAPI heroItemsApi;
    private final HangarManager hangarManager;
    private final Gson gson;
    private final InventoryAPI inventoryApi;

    private long nextCheck = 0L;
    boolean once = true;

    public TechHelper(PluginAPI api, HangarManager hangarManager, BackpageManager backpage) {
        this.heroItemsApi = api.requireAPI(HeroItemsAPI.class);
        this.hangarManager = hangarManager;
        this.inventoryApi = api.requireAPI(InventoryAPI.class);
        this.gson = backpage.getGson();
    }

    @Override
    public void onBackgroundTick() {
        if (nextCheck <= System.currentTimeMillis()) {
            //ItemUseResult result = this.heroItemsApi.useItem(SelectableItem.Tech.PRECISION_TARGETER, 5000);
            //System.out.println(result.isSuccessful());
            if (nextCheck != 0 && once) {
                System.out.println("triggered");
                String xyz = "action=move&params=eyJmcm9tIjp7ImNvbmZpZ0lkIjoyLCJ0YXJnZXQiOiJpbnZlbnRvcnkiLCJpdGVtcyI6WyI4OTA2%0ANDM1NSJdfSwicGFyYW1zIjp7ImhpIjoyODkzOTAyfSwidG8iOnsiY29uZmlnSWQiOjIsInNsb3Rz%0AZXQiOiJnZW5lcmF0b3JzIiwidGFyZ2V0Ijoic2hpcCJ9LCJhY3Rpb24iOiJtb3ZlIn0%3D";
                JsonObject root = new JsonObject();

                // 2. Build the "from" object
                JsonObject fromNode = new JsonObject();
                fromNode.addProperty("configId", 2);
                fromNode.addProperty("target", "inventory");

                JsonArray itemsArray = new JsonArray();
                itemsArray.add("89077388");
                fromNode.add("items", itemsArray);

                root.add("from", fromNode);

                // 3. Build the "params" object
                JsonObject paramsNode = new JsonObject();
                paramsNode.addProperty("hi", 2893902);
                root.add("params", paramsNode);

                // 4. Build the "to" object
                JsonObject toNode = new JsonObject();
                toNode.addProperty("configId", 2);
                toNode.addProperty("slotset", "generators");
                toNode.addProperty("target", "ship");
                root.add("to", toNode);
                // 5. Add the "action" field
                root.addProperty("action", "move");

                // Print the result
                System.out.println(root.toString());
                /*try {
                    InputStream out = this.hangarManager.getInputStream("move", root);
                    HangarResponse hangar;
                    try (var reader = new InputStreamReader(Base64Utils.decodeStream(out))) {
                        hangar = gson.fromJson(reader, HangarResponse.class);
                    }
                    System.out.println(hangar.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                once = false;
            }
        }
        Task.super.onBackgroundTick();
    }

    @Override
    public void onTickTask() {
        if (nextCheck <= System.currentTimeMillis()) {
            var items = this.hangarManager.getItems();
            System.out.println(items);
            for (var item : items) {
                System.out.println(item.getItemId() + ": " + item.getLoot());
            }
            nextCheck = System.currentTimeMillis() + 10_000L;
        }

    }
}
