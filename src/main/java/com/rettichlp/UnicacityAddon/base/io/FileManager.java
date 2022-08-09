package com.rettichlp.UnicacityAddon.base.io;

import com.rettichlp.UnicacityAddon.UnicacityAddon;
import com.rettichlp.UnicacityAddon.base.abstraction.AbstractionLayer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author RettichLP
 */
public class FileManager {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");

    public static File getMinecraftDir() {
        return UnicacityAddon.MINECRAFT.mcDataDir;
    }

    public static File getUnicacityAddonDir() {
        File unicacityAddonDir = new File(getMinecraftDir().getAbsolutePath() + "/unicacityAddon/");
        if (unicacityAddonDir.exists() || unicacityAddonDir.mkdir()) return unicacityAddonDir;

        AbstractionLayer.getPlayer().sendErrorMessage("Ordner 'unicacityAddon' wurde nicht gefunden!");

        return null;
    }

    public static File getAddonScreenshotDir() {
        if (getUnicacityAddonDir() == null) return null;
        File addonScreenshotDir = new File(getUnicacityAddonDir().getAbsolutePath() + "/screenshots/");
        if (addonScreenshotDir.exists() || addonScreenshotDir.mkdir()) return addonScreenshotDir;

        AbstractionLayer.getPlayer().sendErrorMessage("Ordner 'screenshots' wurde nicht gefunden!");

        return null;
    }

    public static File getBlacklistDataFile() throws IOException {
        if (getUnicacityAddonDir() == null) return null;
        File blacklistDataFile = new File(getUnicacityAddonDir().getAbsolutePath() + "/blacklistData.json");
        if (blacklistDataFile.exists() || blacklistDataFile.createNewFile()) return blacklistDataFile;

        AbstractionLayer.getPlayer().sendErrorMessage("Datei 'blacklistData.json' wurde nicht gefunden!");

        return null;
    }

    public static File getDataFile() throws IOException {
        if (getUnicacityAddonDir() == null) return null;
        File dataFile = new File(getUnicacityAddonDir().getAbsolutePath() + "/data.json");
        if (dataFile.exists() || dataFile.createNewFile()) return dataFile;

        AbstractionLayer.getPlayer().sendErrorMessage("Datei 'data.json' wurde nicht gefunden!");

        return null;
    }

    public static File getTodoDataFile() throws IOException {
        if (getUnicacityAddonDir() == null) return null;
        File todoDataFile = new File(getUnicacityAddonDir().getAbsolutePath() + "/todoData.json");
        if (todoDataFile.exists() || todoDataFile.createNewFile()) return todoDataFile;

        AbstractionLayer.getPlayer().sendErrorMessage("Datei 'todoData.json' wurde nicht gefunden!");

        return null;
    }

    public static File getNewImageFile() throws IOException {
        if (getAddonScreenshotDir() == null) return null;

        String date = DATE_FORMAT.format(new Date());
        StringBuilder sb = new StringBuilder(date);
        int i = 1;
        while (new File(getAddonScreenshotDir().getAbsolutePath() + "/" + sb + ".jpg").exists()) {
            if (i == 1) sb.append("_").append(i++);
            else sb.replace(sb.length() - 1, sb.length(), String.valueOf(i));
        }

        File newImageFile = new File(getAddonScreenshotDir().getAbsolutePath() + "/" + sb + ".jpg");
        return newImageFile.createNewFile() ? newImageFile : null;
    }

    public static void loadData() {
        try {
            File dataFile = FileManager.getDataFile();
            if (dataFile == null) return;
            Gson g = new Gson();
            String jsonData = FileUtils.readFileToString(dataFile, StandardCharsets.UTF_8.toString());

            if (jsonData.isEmpty()) {
                BankMoneyModule.setBalance(0);
                CashMoneyModule.setBalance(0);
                JobMoneyModule.setBalance(0);
                PaydayModule.setTime(0);
                TodoListCommand.todolist = Collections.emptyList();
                return;
            }

            Data data = g.fromJson(jsonData, Data.class);
            BankMoneyModule.bankBalance = data.getBankBalance();
            CashMoneyModule.cashBalance = data.getCashBalance();
            JobMoneyModule.jobBalance = data.getJobBalance();
            PaydayModule.currentTime = data.getPaydayTime();
            TodoListCommand.todolist = data.getTodolist();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveData() {
        try {
            File dataFile = FileManager.getDataFile();
            if (dataFile == null) return;
            Gson g = new Gson();
            Data data = new Data();
            data.setBankBalance(BankMoneyModule.bankBalance);
            data.setCashBalance(CashMoneyModule.cashBalance);
            data.setJobBalance(JobMoneyModule.jobBalance);
            data.setPaydayTime(PaydayModule.currentTime);
            data.setTodolist(TodoListCommand.todolist);
            FileUtils.writeStringToFile(dataFile, g.toJson(data), StandardCharsets.UTF_8.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}