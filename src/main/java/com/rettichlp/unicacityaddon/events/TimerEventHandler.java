package com.rettichlp.unicacityaddon.events;

import com.rettichlp.unicacityaddon.base.api.request.APIRequest;
import com.rettichlp.unicacityaddon.base.enums.api.StatisticType;
import com.rettichlp.unicacityaddon.base.manager.FileManager;
import com.rettichlp.unicacityaddon.base.registry.annotation.UCEvent;
import com.rettichlp.unicacityaddon.base.text.PatternHandler;
import com.rettichlp.unicacityaddon.base.utils.ForgeUtils;
import com.rettichlp.unicacityaddon.commands.ShutdownJailCommand;
import com.rettichlp.unicacityaddon.events.faction.rettungsdienst.ReviveEventHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

/**
 * @author RettichLP
 */
@UCEvent
public class TimerEventHandler {

    private static boolean isJail = false;

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent e) {
        String msg = e.getMessage().getUnformattedText();

        Matcher fbiHackStartedMatcher = PatternHandler.TIMER_FBI_HACK_START_PATTERN.matcher(msg);
        if (fbiHackStartedMatcher.find()) {
            FileManager.DATA.setTimer(Integer.parseInt(fbiHackStartedMatcher.group(1)));
            return;
        }

        Matcher timerGraveyardStartMatcher = PatternHandler.TIMER_GRAVEYARD_START_PATTERN.matcher(msg);
        if (timerGraveyardStartMatcher.find()) {
            ReviveEventHandler.isDead = true;
            APIRequest.sendStatisticAddRequest(StatisticType.DEATH);

            if (!isJail) {
                int seconds = (int) TimeUnit.MINUTES.toSeconds(Integer.parseInt(timerGraveyardStartMatcher.group(1)));
                FileManager.DATA.setTimer(seconds);
            }

            return;
        }

        Matcher timerJailStartMatcher = PatternHandler.TIMER_JAIL_START_PATTERN.matcher(msg);
        if (timerJailStartMatcher.find()) {
            isJail = true;
            int seconds = (int) TimeUnit.MINUTES.toSeconds(Integer.parseInt(timerJailStartMatcher.group(1)));
            FileManager.DATA.setTimer(seconds);
            return;
        }

        Matcher jailModifyMatcher = PatternHandler.TIMER_JAIL_MODIFY_PATTERN.matcher(msg);
        if (jailModifyMatcher.find()) {
            FileManager.DATA.setTimer(FileManager.DATA.getTimer() - Integer.parseInt(jailModifyMatcher.group(1)) * 60);
            return;
        }

        Matcher jailFinishMatcher = PatternHandler.TIMER_JAIL_FINISH_PATTERN.matcher(msg);
        if (jailFinishMatcher.find()) {
            isJail = false;
            FileManager.DATA.setTimer(0);

            if (ShutdownJailCommand.shutdownJail)
                ForgeUtils.shutdownPC();
        }
    }
}
