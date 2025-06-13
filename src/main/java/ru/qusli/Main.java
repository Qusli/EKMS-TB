package ru.qusli;

import org.glavo.rcon.Rcon;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.qusli.bot.EKMSBot;
import ru.qusli.config.ConfigLoader;
import ru.qusli.logger.Logger;
import ru.qusli.whitelist.Whitelist;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Logger.log("Application start");

        Properties properties = ConfigLoader.load("application.properties");

        String botToken = properties.get("telegram.api.token").toString();

        String rconHost = properties.get("rcon.host").toString();
        int rconPort = Integer.parseInt(properties.get("rcon.port").toString());
        String rconPassword = properties.get("rcon.password").toString();

        String _whitelist = properties.get("whitelist").toString();

        Whitelist whitelist = new Whitelist(_whitelist);

        try (TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication()){
            // Connected to RCON
            Rcon rcon = new Rcon(rconHost, rconPort, rconPassword);

            Logger.log("Rcon connected");

            // Register TB
            application.registerBot(botToken, new EKMSBot(botToken, rcon, whitelist));

            Logger.log("EKMS registered");

            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}