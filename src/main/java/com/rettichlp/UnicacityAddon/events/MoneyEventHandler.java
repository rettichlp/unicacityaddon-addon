package com.rettichlp.UnicacityAddon.events;

import com.rettichlp.UnicacityAddon.base.abstraction.AbstractionLayer;
import com.rettichlp.UnicacityAddon.base.abstraction.UPlayer;
import com.rettichlp.UnicacityAddon.base.config.ConfigElements;
import com.rettichlp.UnicacityAddon.base.text.PatternHandler;
import com.rettichlp.UnicacityAddon.modules.BankMoneyModule;
import com.rettichlp.UnicacityAddon.modules.JobMoneyModule;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;

/**
 * @author Dimiikou, RettichLP
 */
public class MoneyEventHandler {

    @SubscribeEvent
    public boolean onClientChatReceived(ClientChatReceivedEvent e) {
        String msg = e.getMessage().getUnformattedText();
        UPlayer p = AbstractionLayer.getPlayer();

        Matcher jobSalaryMatcher = PatternHandler.JOB_SALARY_PATTERN.matcher(msg);
        if (jobSalaryMatcher.find()) {
            JobMoneyModule.addBalance(Integer.parseInt(jobSalaryMatcher.group(1)));
            return false;
        }

        Matcher bankNewBalanceMatcher = PatternHandler.BANK_NEW_BALANCE_PATTERN.matcher(msg);
        if (bankNewBalanceMatcher.find()) {
            BankMoneyModule.setBalance(Integer.parseInt(bankNewBalanceMatcher.group(1)));
            return false;
        }

        Matcher bankPaydayMatcher = PatternHandler.BANK_PAYDAY_PATTERN.matcher(msg);
        if (bankPaydayMatcher.find()) {
            BankMoneyModule.setBalance(Integer.parseInt(bankPaydayMatcher.group(1)));
            JobMoneyModule.setBalance(0);
            return false;
        }

        Matcher bankTransferToMatcher = PatternHandler.BANK_TRANSFER_TO_PATTERN.matcher(msg);
        if (bankTransferToMatcher.find()) {
            BankMoneyModule.removeBalance(Integer.parseInt(bankTransferToMatcher.group(2)));
            return false;
        }

        Matcher bankTransferGetMatcher = PatternHandler.BANK_TRANSFER_GET_PATTERN.matcher(msg);
        if (bankTransferGetMatcher.find()) {
            BankMoneyModule.addBalance(Integer.parseInt(bankTransferGetMatcher.group(2)));
            return false;
        }

        Matcher kontoauszugMatcher = PatternHandler.BANK_STATEMENT_PATTERN.matcher(msg);
        if (kontoauszugMatcher.find()) {
            BankMoneyModule.setBalance(Integer.parseInt(kontoauszugMatcher.group(1)));

            if (ConfigElements.getEventATM()) {
                if (ConfigElements.getEventATMFBank()) {
                    p.sendChatMessage("/fbank");
                }

                if (ConfigElements.getEventATMGRKasse()) {
                    p.sendChatMessage("/grkasse info");
                }

                if (ConfigElements.getEventATMInfo()) {
                    p.sendChatMessage("/atminfo");
                }
            }

            return false;
        }

        return false;
    }
}