package ru.qusli;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.qusli.bot.EKMSBot;
import ru.qusli.config.ConfigLoader;
import ru.qusli.logger.Logger;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Logger.log("Telegram bot start");

        Properties properties = ConfigLoader.load("src/main/resources/application.properties");

        String botToken = properties.get("telegram.api.token").toString();

        try (TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication()){
            application.registerBot(botToken, new EKMSBot(botToken));
            Logger.log("EKMS registered");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}