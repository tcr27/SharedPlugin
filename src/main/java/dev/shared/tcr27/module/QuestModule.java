package dev.shared.tcr27.module;

import com.github.manolo8.darkbot.Main;
import com.github.manolo8.darkbot.core.BotInstaller;
import com.github.manolo8.darkbot.core.manager.GuiManager;
import com.github.manolo8.darkbot.core.objects.Gui;
import com.github.manolo8.darkbot.core.objects.facades.QuestProxy;
import com.github.manolo8.darkbot.utils.Time;
import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.game.entities.Station;
import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.api.game.other.Point;
import eu.darkbot.api.managers.*;
import eu.darkbot.shared.modules.LootCollectorModule;
import eu.darkbot.shared.utils.MapTraveler;
import eu.darkbot.util.Timer;

import java.util.List;
import java.util.Optional;

import static com.github.manolo8.darkbot.Main.API;

@Feature(name = "Quest Module", description = "Does your quests.")
public class QuestModule extends LootCollectorModule {

    private final StarSystemAPI starSystemAPI;
    private final MapTraveler mapTraveler;
    private final EntitiesAPI entities;
    private final HeroAPI heroApi;
    private final QuestAPI questApi;
    private final GuiManager guiManager;
    private final BotInstaller botInstaller;

    private long nextCheck = 0L;
    private long nextCheck1 = 0L;
    private long initializeQuests = 0L;
    private boolean onHold = true;
    private boolean flag = true;
    private boolean questSelected = false;


    public QuestModule(PluginAPI api, GuiManager guiManager, MapTraveler mapTraveler) {
        super(api);
        this.guiManager = guiManager;
        this.mapTraveler = mapTraveler;
        this.botInstaller = api.requireInstance(BotInstaller.class);
        this.starSystemAPI = api.requireAPI(StarSystemAPI.class);
        this.entities = api.requireAPI(EntitiesAPI.class);
        this.heroApi = api.requireAPI(HeroAPI.class);
        this.questApi = api.requireAPI(QuestAPI.class);
    }

    @Override
    public void onTickModule() {
        if (System.currentTimeMillis() >= nextCheck) {
            hasQuests();
            acceptQuests();
            /*if (!hasQuests()) {
                onHold = true;
            } else {
                onHold = false;
            }*/

            nextCheck = System.currentTimeMillis() + 2_000L;
        }
        //super.onTickModule();
    }

    private boolean hasQuests() {

        Gui questsGui = this.guiManager.getGui("quests");
        if (questsGui == null) {
            return false;
        }
        int width = (int) Math.round(questsGui.readDouble(0x1f8));
        int height = (int) Math.round(questsGui.readDouble(0x200));
        int runningQuests = API.readInt(botInstaller.mainAddress.get(), 0x208, 0x020, 0x024);
        for (int i = 1; i <= runningQuests; i++) {
            int cx = width - i * 11;
            questsGui.click(cx, 5);
            Time.sleep(25);
        }
        System.out.println(runningQuests);


        return false;
    }

    private void acceptQuests() {
        if (this.heroApi.isAttacking()) {
            this.heroApi.triggerLaserAttack();
        }
        Optional<GameMap> xyz = this.starSystemAPI.findMap("3-8");
        if (xyz.isEmpty()) {
            return;
        }
        GameMap targetMap = xyz.get();
        int currentMapId = this.starSystemAPI.getCurrentMap().getId();
        if (currentMapId != targetMap.getId()) {
            this.mapTraveler.setTarget(targetMap);
            this.mapTraveler.tick();
            return;
        }
        Station.QuestGiver questGiverStation = this.entities.getStations().stream()
                .filter(station -> station instanceof Station.QuestGiver)
                .map(station -> (Station.QuestGiver) station)
                .filter(Station::isValid)
                .findFirst()
                .orElse(null);
        if (questGiverStation == null) {
            log("not found");
            return;
        }
        double distance = this.hero.distanceTo(questGiverStation);
        if (distance > 300) {
            this.movement.moveTo(questGiverStation);
            return;
        }
        this.movement.stop(false);
        /*if (questGiverStation.isSelectable() && !this.questApi.isQuestGiverOpen()) {
            questGiverStation.trySelect(true);
        }
        if (!this.questApi.isQuestGiverOpen()) {
            return;
        }*/
        if (System.currentTimeMillis() >= nextCheck1) {
            if (flag) {
                this.movement.moveTo(questGiverStation.getX() + 10, questGiverStation.getY() + 10);
            } else {
                this.movement.moveTo(questGiverStation.getX() - 10, questGiverStation.getY() - 10);
            }
            flag = !flag;
            nextCheck1 = System.currentTimeMillis() + 10_000L;
        }
/*
        Point questListItem01 = getOffsetPoint(208, 340);
        Point questListItem02 = getOffsetPoint(208, 370);
        Point questListItem03 = getOffsetPoint(208, 410);
        Point questListItem04 = getOffsetPoint(208, 450);
        Point questListItem05 = getOffsetPoint(208, 480);
        Point questListItem06 = getOffsetPoint(208, 510);
        Point upArrow = getOffsetPoint(258, 340);
        Point downArrow = getOffsetPoint(258, 530);
        Point accept_abortPtn = getOffsetPoint(830, 510);
        if (!questSelected)
            this.windowApi.mouseClick(questListItem04);
        else
            this.windowApi.mouseClick(accept_abortPtn);
        this.questSelected = !this.questSelected;
*/



/*
        //Point x = Point.of(212 + 28, 105 + x1);
        Point x = Point.of(212 + 128, 550);
        log("click on: " + x.getX() + " - " + x.getY());
        this.windowApi.mouseClick(x);

        x = Point.of(470, 290);
        log("click on: " + x.getX() + " - " + x.getY());
        this.windowApi.mouseClick(x);

        Point accept_abortPtn = Point.of(212 + 800, 115 + 490);


        //    log("click on accept/close: " + accept_abortPtn.getX() + " - " + accept_abortPtn.getY());
        //this.windowApi.mouseClick(accept_abortPtn);
*/

    }

    private Point getOffsetPoint(int x, int y) {
        return Point.of(212 + x, 105 + y);
    }

    private void log(Object msg) {
        System.out.println(msg);
    }
}
