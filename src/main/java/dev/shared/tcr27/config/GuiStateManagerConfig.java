package dev.shared.tcr27.config;

import eu.darkbot.api.config.annotations.Number;
import eu.darkbot.api.config.annotations.Option;

import java.util.Map;

public class GuiStateManagerConfig {
    @Option("tcr27.guistatemanager.checkInterval")
    @Number(min = 5, max = 1440, step = 5)
    public int checkInterval = 60;
    @Option("tcr27.guistatemanager.guis")
    public GuiStates guiStates = new GuiStates();

    public static class GuiStates {
        @Option("tcr27.guistatemanager.guis.ship")
        public boolean ship = false;
        @Option("tcr27.guistatemanager.guis.minimap")
        public boolean minimap = false;
        @Option("tcr27.guistatemanager.guis.pet")
        public boolean pet = false;
        @Option("tcr27.guistatemanager.guis.quests")
        public boolean quests = false;
        @Option("tcr27.guistatemanager.guis.booster")
        public boolean booster = false;
        @Option("tcr27.guistatemanager.guis.user")
        public boolean user = false;
        @Option("tcr27.guistatemanager.guis.group")
        public boolean group = false;

        public Map<String, Boolean> asMap() {
            return Map.of("ship", ship, "minimap", minimap, "pet", pet, "quests", quests, "booster", booster, "user", user, "group", group);
        }
    }
}
