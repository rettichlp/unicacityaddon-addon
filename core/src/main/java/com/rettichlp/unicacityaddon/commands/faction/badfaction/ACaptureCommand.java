package com.rettichlp.unicacityaddon.commands.faction.badfaction;

import com.rettichlp.unicacityaddon.UnicacityAddon;
import com.rettichlp.unicacityaddon.base.annotation.UCCommand;
import com.rettichlp.unicacityaddon.base.builder.TabCompletionBuilder;
import net.labymod.api.client.chat.command.Command;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Dimiikou
 */
@UCCommand
public class ACaptureCommand extends Command {

    public static boolean isActive;

    private static final String usage = "/capture";

    private final UnicacityAddon unicacityAddon;

    public ACaptureCommand(UnicacityAddon unicacityAddon) {
        super("capture");
        this.unicacityAddon = unicacityAddon;
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        if (isActive)
            return true;

        this.unicacityAddon.player().sendServerMessage("/capture");
        isActive = true;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ACaptureCommand.this.unicacityAddon.player().sendServerMessage("/capture");
                isActive = false;
            }
        }, TimeUnit.SECONDS.toMillis(15));
        return true;
    }

    @Override
    public List<String> complete(String[] arguments) {
        return TabCompletionBuilder.getBuilder(this.unicacityAddon, arguments).build();
    }
}