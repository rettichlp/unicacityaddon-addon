package com.rettichlp.UnicacityAddon.base.abstraction;

import com.rettichlp.UnicacityAddon.UnicacityAddon;
import com.rettichlp.UnicacityAddon.base.faction.Faction;
import com.rettichlp.UnicacityAddon.base.faction.FactionHandler;
import com.rettichlp.UnicacityAddon.base.faction.rettungsdienst.Medication;
import com.rettichlp.UnicacityAddon.base.text.ColorCode;
import com.rettichlp.UnicacityAddon.base.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.inventory.Container;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.UUID;

/**
 * @author Fuzzlemann
 */
public class UPlayerImpl implements UPlayer {

    @Override public EntityPlayerSP getPlayer() {
        return UnicacityAddon.MINECRAFT.player;
    }

    @Override public boolean isConnected() {
        return getPlayer() != null;
    }

    @Override public void sendMessage(ITextComponent textComponent) {
        getPlayer().sendMessage(textComponent);
    }

    @Override public void sendErrorMessage(String message) {
        sendMessage(Message.getBuilder()
                .error().space()
                .of(message).color(ColorCode.GRAY).advance()
                .createComponent());
    }

    @Override public void sendInfoMessage(String message) {
        sendMessage(Message.getBuilder()
                .info().space()
                .of(message).color(ColorCode.WHITE).advance()
                .createComponent());
    }

    @Override public void sendSyntaxMessage(String message) {
        sendErrorMessage("Syntax: " + message);
    }

    @Override public void sendMessageAsString(String message) {
        getPlayer().sendMessage(new TextComponentString(message));
    }

    @Override public void sendChatMessage(String message) {
        getPlayer().sendChatMessage(message);
    }

    @Override public void playSound(SoundEvent soundIn, float volume, float pitch) {
        getPlayer().playSound(soundIn, volume, pitch);
    }

    @Override public String getName() {
        return UnicacityAddon.MINECRAFT.getSession().getUsername();
    }

    @Override public NetworkPlayerInfo getNetworkPlayerInfo() {
        if (UnicacityAddon.MINECRAFT.getConnection() == null) return null;
        return UnicacityAddon.MINECRAFT.getConnection().getPlayerInfo(getUniqueID());
    }

    @Override public UUID getUniqueID() {
        return UnicacityAddon.MINECRAFT.getSession().getProfile().getId();
    }

    @Override public BlockPos getPosition() {
        return getPlayer().getPosition();
    }

    @Override public Team getTeam() {
        return getPlayer().getTeam();
    }

    @Override public Scoreboard getWorldScoreboard() {
        return getPlayer().getWorldScoreboard();
    }

    @Override public World getWorld() {
        return getPlayer().getEntityWorld();
    }

    @Override public Container getOpenContainer() {
        return getPlayer().openContainer;
    }

    @Override public Container getInventoryContainer() {
        return getPlayer().inventoryContainer;
    }

    @Override public AxisAlignedBB getEntityBoundingBox() {
        return getPlayer().getEntityBoundingBox();
    }

    @Override public NetHandlerPlayClient getConnection() {
        return getPlayer().connection;
    }

    @Override public Faction getFaction() {
        return FactionHandler.getPlayerFactionMap().get(getName());
    }

    @Override public void sellMedication(String target, Medication medication) {
        sendChatMessage("/rezept " + target + " " + medication.name());
    }

    @Override public void acceptOffer() {
        sendChatMessage("/annehmen");
    }

    @Override public void stopRoute() {
        sendChatMessage("/stoproute");
    }

    @Override public void setNaviRoute(int x, int y, int z) {
        sendChatMessage("/navi " + x + "/" + y + "/" + z);
    }

    @Override public void setNaviRoute(BlockPos blockPos) {
        sendChatMessage("/navi " + blockPos.getX() + "/" + blockPos.getY() + "/" + blockPos.getZ());
    }

    @Override public void copyToClipboard(String string) {
        StringSelection stringSelection = new StringSelection(string);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}