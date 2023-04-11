package com.rettichlp.unicacityaddon.listener;

import com.rettichlp.unicacityaddon.UnicacityAddon;
import com.rettichlp.unicacityaddon.base.annotation.UCEvent;
import com.rettichlp.unicacityaddon.base.text.FormattingCode;
import com.rettichlp.unicacityaddon.base.text.Message;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamUpdateEvent;

/**
 * @author RettichLP
 */
@UCEvent
public class NameTagRenderListener {

    private final UnicacityAddon unicacityAddon;

    public NameTagRenderListener(UnicacityAddon unicacityAddon) {
        this.unicacityAddon = unicacityAddon;
    }

    /**
     * Quote: "Wenn ich gleich nicht mehr antworte, einfach laut meinen Namen sagen." - Lou, 02.10.2022
     * "Fällst du dann aus dem Bett?" - RettichLP und Ullrich, 02.10.2022
     */
    @Subscribe
    public void onPlayerNameTagRender(PlayerNameTagRenderEvent e) {
        if (e.context().equals(PlayerNameTagRenderEvent.Context.PLAYER_RENDER)) {
            NetworkPlayerInfo networkPlayerInfo = e.playerInfo();

            if (networkPlayerInfo != null) {
                String playerName = networkPlayerInfo.profile().getUsername();

                if (this.unicacityAddon.nametagService().getMaskedPlayerList().contains(playerName)) {
                    e.setNameTag(Message.getBuilder().of(playerName).obfuscated().advance().createComponent());
                } else {
                    String prefix = this.unicacityAddon.nametagService().getPrefix(playerName, false);
                    if (!prefix.equals(FormattingCode.RESET.getCode())) {
                        // prevent to add the pencil to players whose name was not visible changed
                        e.setNameTag(Message.getBuilder().add(prefix + playerName).createComponent());
                    }
                }
            }
        }
    }

    @Subscribe
    public void onScoreboardTeamUpdate(ScoreboardTeamUpdateEvent e) {
        this.unicacityAddon.player().getScoreboard().getTeams().stream()
                .filter(scoreboardTeam -> scoreboardTeam.getTeamName().equals("nopush"))
                .findFirst()
                .ifPresent(scoreboardTeam -> this.unicacityAddon.nametagService().setNoPushPlayerList(scoreboardTeam.getEntries()));

        this.unicacityAddon.player().getScoreboard().getTeams().stream()
                .filter(scoreboardTeam -> scoreboardTeam.getTeamName().equals("masked"))
                .findFirst()
                .ifPresent(scoreboardTeam -> this.unicacityAddon.nametagService().setMaskedPlayerList(scoreboardTeam.getEntries()));
    }
}