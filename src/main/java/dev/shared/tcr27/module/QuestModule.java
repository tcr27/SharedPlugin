package dev.shared.tcr27.module;

import com.github.manolo8.darkbot.core.manager.GuiManager;
import com.github.manolo8.darkbot.core.objects.facades.QuestProxy;
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

@Feature(name = "Quest Module", description = "Does your quests.")
public class QuestModule extends LootCollectorModule {

    private final StarSystemAPI starSystemAPI;
    private final MapTraveler mapTraveler;
    private final EntitiesAPI entities;
    private final HeroAPI heroApi;
    private final QuestAPI questApi;
    private final WindowAPI windowApi;
    private long nextCheck = 0L;
    private long nextCheck1 = 0L;
    private boolean onHold = false;
    private boolean flag = true;
    private boolean questSelected = false;


    private Timer loadQuestGiverTimer = Timer.get(1_000L);
    private Timer firstQuestTimer = Timer.get(1_000L);
    private Timer scrollQuestTimer = Timer.get(1_000L);

    public QuestModule(PluginAPI api, MapTraveler mapTraveler) {
        super(api);
        this.mapTraveler = mapTraveler;
        this.starSystemAPI = api.requireAPI(StarSystemAPI.class);
        this.entities = api.requireAPI(EntitiesAPI.class);
        this.heroApi = api.requireAPI(HeroAPI.class);
        this.questApi = api.requireAPI(QuestAPI.class);
        this.windowApi = api.requireAPI(WindowAPI.class);
    }

    @Override
    public void onTickModule() {
        if (System.currentTimeMillis() >= nextCheck) {
            if (!hasQuests()) {
                acceptQuests();
                onHold = true;
            } else {
                onHold = false;
            }

            nextCheck = System.currentTimeMillis() + 1_000L;
        }
        if (!onHold) {
            super.onTickModule();
        }
    }

    private boolean hasQuests() {
        List<? extends QuestAPI.QuestListItem> quests = this.questApi.getCurrestQuests();
        boolean hasActiveQuest = false;
        if (quests == null) {
            return hasActiveQuest;
        }

        for (QuestAPI.QuestListItem quest : quests) {
            if (!quest.isActivable())
                continue;
            log(quest.getTitle());
        }

        return false;
    }

    private void acceptQuests() {
        if (this.heroApi.isAttacking()) {
            this.heroApi.triggerLaserAttack();
        }
        Optional<GameMap> xyz = this.starSystemAPI.findMap("3-4");
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
        if (questGiverStation.isSelectable() && !this.questApi.isQuestGiverOpen()) {
            questGiverStation.trySelect(true);
        }
        if (!this.questApi.isQuestGiverOpen()) {
            return;
        }
        if (System.currentTimeMillis() >= nextCheck1) {
            if (flag) {
                this.movement.moveTo(questGiverStation.getX() + 10, questGiverStation.getY() + 10);
            } else {
                this.movement.moveTo(questGiverStation.getX() - 10, questGiverStation.getY() - 10);
            }
            flag = !flag;
            nextCheck1 = System.currentTimeMillis() + 10_000L;
        }

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
