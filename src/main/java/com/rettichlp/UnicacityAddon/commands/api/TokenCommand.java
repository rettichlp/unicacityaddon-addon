package com.rettichlp.UnicacityAddon.commands.api;

import com.google.gson.JsonObject;
import com.rettichlp.UnicacityAddon.base.abstraction.AbstractionLayer;
import com.rettichlp.UnicacityAddon.base.abstraction.UPlayer;
import com.rettichlp.UnicacityAddon.base.api.TokenManager;
import com.rettichlp.UnicacityAddon.base.api.request.APIRequest;
import com.rettichlp.UnicacityAddon.base.registry.annotation.UCCommand;
import com.rettichlp.UnicacityAddon.base.text.ColorCode;
import com.rettichlp.UnicacityAddon.base.text.Message;
import net.labymod.main.LabyMod;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author RettichLP
 */
@UCCommand
public class TokenCommand implements IClientCommand {

    @Override
    @Nonnull
    public String getName() {
        return "token";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/token (create|revoke)";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) {
        UPlayer p = AbstractionLayer.getPlayer();

        if (args.length == 0) {
            p.sendMessage(Message.getBuilder()
                    .prefix()
                    .of("Mit diesem").color(ColorCode.GRAY).advance().space()
                    .of("Token").color(ColorCode.AQUA)
                            .hoverEvent(HoverEvent.Action.SHOW_TEXT, Message.getBuilder().of(TokenManager.API_TOKEN).color(ColorCode.RED).advance().createComponent())
                            .clickEvent(ClickEvent.Action.RUN_COMMAND, "/token copy")
                            .advance().space()
                    .of("kann jeder in deinem Namen Anfragen an die API senden.").color(ColorCode.GRAY).advance()
                    .createComponent());
        } else if (args.length == 1 && args[0].equalsIgnoreCase("create")) {
            JsonObject response = APIRequest.sendTokenCreateRequest();
            if (response == null) return;
            p.sendAPIMessage(response.get("info").getAsString(), true);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("copy")) {
            p.copyToClipboard(TokenManager.API_TOKEN);
            LabyMod.getInstance().notifyMessageRaw(ColorCode.GREEN.getCode() + "Kopiert!", "Token in Zwischenablage kopiert.");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("revoke")) {
            JsonObject response = APIRequest.sendTokenRevokeRequest();
            if (response == null) return;
            p.sendAPIMessage(response.get("info").getAsString(), true);
        } else {
            p.sendSyntaxMessage(getUsage(sender));
        }
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> tabCompletions = new ArrayList<>();
        if (args.length == 1) {
            tabCompletions.add("create");
            tabCompletions.add("revoke");
        }
        String input = args[args.length - 1].toLowerCase();
        tabCompletions.removeIf(tabComplete -> !tabComplete.toLowerCase().startsWith(input));
        return tabCompletions;
    }

    @Override
    public boolean isUsernameIndex(@Nonnull String[] args, int index) {
        return false;
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    @Override
    public int compareTo(@Nonnull ICommand o) {
        return 0;
    }
}