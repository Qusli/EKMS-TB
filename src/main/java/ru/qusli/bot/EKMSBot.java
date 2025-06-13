package ru.qusli.bot;

import org.glavo.rcon.Rcon;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.qusli.logger.Logger;
import ru.qusli.whitelist.Whitelist;

import java.io.IOException;

public final class EKMSBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient _client;
    private final Rcon _rcon;
    private final Whitelist _whitelist;

    public EKMSBot(String token, Rcon rcon, Whitelist whitelist) {
        this._client = new OkHttpTelegramClient(token);
        this._rcon = rcon;
        this._whitelist = whitelist;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long userId = update.getMessage().getFrom().getId();

            if (this._whitelist.userInWhitelist(userId)) {
                this._executeRconCommand(update);
            } else {
                this._executeNotPermission(update);
            }
        }
    }

    private void _executeNotPermission(Update update) {
        long userId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();

        try {
            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text("У вас недостаточно прав на использование данных комманд!")
                    .build();

            this._client.execute(message);

            Logger.log("ChatId: " + chatId + " - Сообщение отослано пользователю с UserId: " + userId);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void _executeRconCommand(Update update) {
        long userId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        try {
            String result = this._rcon.command(messageText);

            Logger.log("ChatId: " + chatId + " - Команда успешно выполнена");

            result = this._removeUnknowSymbol(result);

            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(result)
                    .build();

            this._client.execute(message);

            Logger.log("ChatId: " + chatId + " - Сообщение отослано пользователю с UserId: " + userId);
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    };

    private String _removeUnknowSymbol(String text) {
        return text.replaceAll("§.", "");
    }
}
