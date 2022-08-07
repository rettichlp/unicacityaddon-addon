package com.rettichlp.UnicacityAddon.modules;

import com.google.gson.Gson;
import com.rettichlp.UnicacityAddon.base.io.FileManager;
import com.rettichlp.UnicacityAddon.base.json.balance.Balance;
import com.rettichlp.UnicacityAddon.base.module.UCModuleHandler;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author RettichLP
 */
public class CashModule extends SimpleModule {

    public static int cash;

    @Override
    public String getControlName() {
        return "Geld auf der Hand";
    }

    @Override
    public String getSettingName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Bargeld";
    }

    @Override
    public String getDisplayValue() {
        return cash + "$";
    }

    @Override
    public String getDefaultValue() {
        return "0";
    }

    @Override
    public String getDescription() {
        return "Zeigt an, wie viel Geld du auf der Hand hast.";
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.GOLD_INGOT);
    }

    @Override
    public ModuleCategory getCategory() {
        return UCModuleHandler.UNICACITY;
    }

    @Override
    public boolean isShown() {
        return cash > 0;
    }

    @Override
    public int getSortingId() {
        return 0;
    }

    @Override
    public void loadSettings() {
    }

    public static void addBalance(int balance) {
        cash = cash + balance;
        saveBalance();
    }

    public static void removeBalance(int balance) {
        cash = cash - balance;
        saveBalance();
    }

    public static void setBalance(int balance) {
        cash = balance;
        saveBalance();
    }

    private static void saveBalance() {
        try {
            File balanceDataFile = FileManager.getBalanceDataFile();
            if (balanceDataFile == null) return;
            Gson g = new Gson();
            Balance balance = new Balance();
            balance.setCash(cash);
            FileUtils.writeStringToFile(balanceDataFile, g.toJson(balance), StandardCharsets.UTF_8.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadBalance() {
        try {
            File balanceDataFile = FileManager.getBalanceDataFile();
            if (balanceDataFile == null) return;
            Gson g = new Gson();
            String jsonData = FileUtils.readFileToString(balanceDataFile, StandardCharsets.UTF_8.toString());

            if (jsonData.isEmpty()) {
                cash = 0;
                return;
            }

            cash = g.fromJson(jsonData, Balance.class).getCash();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}