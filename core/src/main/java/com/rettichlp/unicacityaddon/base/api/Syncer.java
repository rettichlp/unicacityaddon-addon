package com.rettichlp.unicacityaddon.base.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rettichlp.unicacityaddon.base.abstraction.AbstractionLayer;
import com.rettichlp.unicacityaddon.base.api.request.APIRequest;
import com.rettichlp.unicacityaddon.base.enums.api.AddonGroup;
import com.rettichlp.unicacityaddon.base.enums.faction.Faction;
import com.rettichlp.unicacityaddon.base.models.BlacklistReasonEntry;
import com.rettichlp.unicacityaddon.base.models.BroadcastEntry;
import com.rettichlp.unicacityaddon.base.models.HouseBanEntry;
import com.rettichlp.unicacityaddon.base.models.HouseBanReasonEntry;
import com.rettichlp.unicacityaddon.base.models.NaviPointEntry;
import com.rettichlp.unicacityaddon.base.models.PlayerGroupEntry;
import com.rettichlp.unicacityaddon.base.models.WantedReasonEntry;
import com.rettichlp.unicacityaddon.base.text.PatternHandler;
import com.rettichlp.unicacityaddon.base.utils.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Syncer {

    public static final Map<String, Faction> PLAYERFACTIONMAP = new HashMap<>();
    public static final Map<String, Integer> PLAYERRANKMAP = new HashMap<>();
    public static List<HouseBanEntry> HOUSEBANENTRYLIST = new ArrayList<>();
    public static List<NaviPointEntry> NAVIPOINTLIST = new ArrayList<>();

    public static void syncAll() {
        new Thread(() -> {
            Thread t1 = syncPlayerAddonGroupMap();
            Thread t2 = syncPlayerFactionData();
            Thread t3 = syncHousebanEntryList();
            Thread t4 = syncNaviPointEntryList();

            try {
                t1.start();
                t1.join();

                t2.start();
                t2.join();

                t3.start();
                t3.join();

                t4.start();
                t4.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static Thread syncPlayerAddonGroupMap() {
        return new Thread(() -> {
            JsonObject response = APIRequest.sendPlayerRequest();
            if (response != null) {
                for (AddonGroup addonGroup : AddonGroup.values()) {
                    addonGroup.getMemberList().clear();
                    for (JsonElement jsonElement : response.getAsJsonArray(addonGroup.getApiName())) {
                        addonGroup.getMemberList().add(jsonElement.getAsJsonObject().get("name").getAsString());
                    }
                }
            }
//            Laby.labyAPI().minecraft().executeOnRenderThread(() -> UnicacityAddon.ADDON.labyAPI().notificationController().push(Notification.builder()
//                    .title(Component.text("Synchronisierung", NamedTextColor.AQUA))
//                    .text(Component.text("Addon Gruppen aktualisiert."))
//                    .build()));
        });
    }

    public static Thread syncPlayerFactionData() {
        return new Thread(() -> {
            PLAYERFACTIONMAP.clear();
            PLAYERRANKMAP.clear();
            for (Faction faction : Faction.values()) {
                String factionWebsiteSource = faction.getWebsiteSource();
                List<String> nameList = ListUtils.getAllMatchesFromString(PatternHandler.NAME_PATTERN, factionWebsiteSource);
                List<String> rankList = ListUtils.getAllMatchesFromString(PatternHandler.RANK_PATTERN, factionWebsiteSource);
                nameList.forEach(name -> {
                    String formattedname = name.replace("<h4 class=\"h5 g-mb-5\"><strong>", "");
                    PLAYERFACTIONMAP.put(formattedname, faction);
                    PLAYERRANKMAP.put(formattedname, Integer.parseInt(String.valueOf(rankList.get(nameList.indexOf(name))
                            .replace("<strong>Rang ", "")
                            .charAt(0))));
                });
            }

            getPlayerGroupEntryList("LEMILIEU").forEach(playerGroupEntry -> PLAYERFACTIONMAP.put(playerGroupEntry.getName(), Faction.LEMILIEU));

//            Laby.labyAPI().notificationController().push(Notification.builder()
//                    .title(Component.text("Synchronisierung", NamedTextColor.AQUA))
//                    .text(Component.text("Fraktionen aktualisiert."))
//                    .build());
        });
    }

    public static Thread syncHousebanEntryList() {
        return new Thread(() -> {
            HOUSEBANENTRYLIST = getHouseBanEntryList();
//            Laby.labyAPI().notificationController().push(Notification.builder()
//                    .title(Component.text("Synchronisierung", NamedTextColor.AQUA))
//                    .text(Component.text("Hausverbote aktualisiert."))
//                    .build());
        });
    }

    public static Thread syncNaviPointEntryList() {
        return new Thread(() -> {
            NAVIPOINTLIST = getNaviPointEntryList();
//            Laby.labyAPI().notificationController().push(Notification.builder()
//                    .title(Component.text("Synchronisierung", NamedTextColor.AQUA))
//                    .text(Component.text("Navipunkte aktualisiert."))
//                    .build());
        });
    }

    public static List<BlacklistReasonEntry> getBlacklistReasonEntryList() {
        JsonArray response = APIRequest.sendBlacklistReasonRequest();
        if (response == null)
            return new ArrayList<>();
        List<BlacklistReasonEntry> blacklistReasonEntryList = new ArrayList<>();
        response.forEach(jsonElement -> {
            JsonObject o = jsonElement.getAsJsonObject();

            int kills = o.get("kills").getAsInt();
            String reason = o.get("reason").getAsString();
            String issuerUUID = o.get("issuerUUID").getAsString();
            int price = o.get("price").getAsInt();
            String issuerName = o.get("issuerName").getAsString();

            blacklistReasonEntryList.add(new BlacklistReasonEntry(kills, reason, issuerUUID, price, issuerName));
        });
        return blacklistReasonEntryList;
    }

    public static List<BroadcastEntry> getBroadcastEntryList() {
        JsonArray response = APIRequest.sendBroadcastQueueRequest();
        if (response == null)
            return new ArrayList<>();
        List<BroadcastEntry> broadcastEntryList = new ArrayList<>();
        response.forEach(jsonElement -> {
            JsonObject o = jsonElement.getAsJsonObject();

            String broadcast = o.get("broadcast").getAsString();
            int id = o.get("id").getAsInt();
            String issuerName = o.get("issuerName").getAsString();
            String issuerUUID = o.get("issuerUUID").getAsString();
            long sendTime = o.get("sendTime").getAsLong();
            long time = o.get("time").getAsLong();

            broadcastEntryList.add(new BroadcastEntry(broadcast, id, issuerName, issuerUUID, sendTime, time));
        });
        return broadcastEntryList;
    }

    public static List<HouseBanEntry> getHouseBanEntryList() {
        JsonArray response = APIRequest.sendHouseBanRequest(AbstractionLayer.getPlayer().getFaction().equals(Faction.RETTUNGSDIENST));
        if (response == null)
            return new ArrayList<>();
        List<HouseBanEntry> houseBanEntryList = new ArrayList<>();
        response.forEach(jsonElement -> {
            JsonObject o = jsonElement.getAsJsonObject();

            long duration = o.get("duration").getAsLong();
            List<HouseBanReasonEntry> houseBanReasonList = new ArrayList<>();
            long expirationTime = o.get("expirationTime").getAsLong();
            String name = o.get("name").getAsString();
            long startTime = o.get("startTime").getAsLong();
            String uuid = o.get("uuid").getAsString();

            o.get("houseBanReasonList").getAsJsonArray().forEach(jsonElement1 -> {
                JsonObject o1 = jsonElement1.getAsJsonObject();

                String reason = o1.get("reason").getAsString();
                String issuerUUID = o1.has("issuerUUID") ? o1.get("issuerUUID").getAsString() : null;
                String issuerName = o1.has("issuerName") ? o1.get("issuerName").getAsString() : null;
                int days = o1.get("days").getAsInt();

                houseBanReasonList.add(new HouseBanReasonEntry(reason, issuerUUID, issuerName, days));
            });

            houseBanEntryList.add(new HouseBanEntry(duration, houseBanReasonList, expirationTime, name, startTime, uuid));
        });
        return houseBanEntryList;
    }

    public static List<HouseBanReasonEntry> getHouseBanReasonEntryList() {
        JsonArray response = APIRequest.sendHouseBanReasonRequest();
        if (response == null)
            return new ArrayList<>();
        List<HouseBanReasonEntry> houseBanReasonEntryList = new ArrayList<>();
        response.forEach(jsonElement -> {
            JsonObject o = jsonElement.getAsJsonObject();

            String reason = o.get("reason").getAsString();
            String creatorUUID = o.has("creatorUUID") ? o.get("creatorUUID").getAsString() : null;
            String creatorName = o.has("creatorName") ? o.get("creatorName").getAsString() : null;
            int days = o.get("days").getAsInt();

            houseBanReasonEntryList.add(new HouseBanReasonEntry(reason, creatorUUID, creatorName, days));
        });
        return houseBanReasonEntryList;
    }

    public static List<NaviPointEntry> getNaviPointEntryList() {
        JsonArray response = APIRequest.sendNaviPointRequest();
        if (response == null)
            return new ArrayList<>();
        List<NaviPointEntry> naviPointEntryList = new ArrayList<>();
        response.forEach(jsonElement -> {
            JsonObject o = jsonElement.getAsJsonObject();

            String name = o.get("name").getAsString();
            int x = o.get("x").getAsInt();
            int y = o.get("y").getAsInt();
            int z = o.get("z").getAsInt();
            String article = o.get("article").getAsString();

            naviPointEntryList.add(new NaviPointEntry(name, x, y, z, article));
        });
        return naviPointEntryList;
    }

    public static List<String> getPlayerGroupList() {
        JsonArray response = APIRequest.sendPlayerGroupRequest();
        List<String> playerGroupList = new ArrayList<>();
        if (response != null) {
            response.forEach(jsonElement -> playerGroupList.add(jsonElement.getAsString()));
        }
        return playerGroupList;
    }

    public static List<PlayerGroupEntry> getPlayerGroupEntryList(String group) {
        JsonObject response = APIRequest.sendPlayerRequest();
        if (response == null || !response.has(group))
            return new ArrayList<>();
        List<PlayerGroupEntry> playerGroupEntryList = new ArrayList<>();
        response.get(group).getAsJsonArray().forEach(jsonElement -> {
            JsonObject o = jsonElement.getAsJsonObject();

            String name = o.get("name").getAsString();
            String uuid = o.get("uuid").getAsString();

            playerGroupEntryList.add(new PlayerGroupEntry(name, uuid));
        });
        return playerGroupEntryList;
    }

    public static List<WantedReasonEntry> getWantedReasonEntryList() {
        JsonArray response = APIRequest.sendWantedReasonRequest();
        if (response == null)
            return new ArrayList<>();
        List<WantedReasonEntry> wantedReasonEntryList = new ArrayList<>();
        response.forEach(jsonElement -> {
            JsonObject o = jsonElement.getAsJsonObject();

            String reason = o.get("reason").getAsString();
            String creatorUUID = o.get("creatorUUID").getAsString();
            String creatorName = o.get("creatorName").getAsString();
            int points = o.get("points").getAsInt();

            wantedReasonEntryList.add(new WantedReasonEntry(reason, creatorUUID, creatorName, points));
        });
        return wantedReasonEntryList;
    }
}