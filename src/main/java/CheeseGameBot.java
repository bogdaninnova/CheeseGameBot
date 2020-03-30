import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class CheeseGameBot extends TelegramLongPollingBot {

    private UsersList usersList = new UsersList();
    private String token = "";
    private CheeseGame cheeseGame;

    public CheeseGameBot() {
        try {
            FileInputStream fileInput = new FileInputStream(new File("src\\main\\resources\\token.properties"));
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
            token = properties.getProperty("token");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        long chatId;

        if (update.getCallbackQuery() != null) {



            chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();
            String userName = update.getCallbackQuery().getFrom().getUserName();

            if (!cheeseGame.getCurrentPlayer().equals(userName))
                return;

            int position = Integer.parseInt(data.substring(5, 6));
            int chosenValue = cheeseGame.getTable().get(position).getValue();
            CheeseCard cheeseCard;

            switch (data.substring(0, 5)) {
                case "throw" :
                    if (chosenValue != cheeseGame.getDie())
                        return;
                    cheeseGame.turn(userName, position, false);
                    sendTable(chatId);
                    break;
                case "get__" :
                    if (chosenValue != cheeseGame.getDie())
                        return;
                    cheeseCard = cheeseGame.turn(userName, position, true);
                    sendSimpleMessage("You get " + cheeseCard.getValue() + " card, isTrap=" + cheeseCard.isTrap(), usersList.allUsers.get(userName));
                    sendTable(chatId);
                    break;
                case "check" :
                    cheeseCard = cheeseGame.getTable().get(position);
                    sendSimpleMessage("You checked " + cheeseCard.getValue() + " card, isTrap=" + cheeseCard.isTrap(), usersList.allUsers.get(userName));
                    cheeseGame.skipTurn();
                    sendTable(chatId);
                break;
            }

        } else {
            String text = update.getMessage().getText();
            User user = update.getMessage().getFrom();
            chatId = update.getMessage().getChatId();

//        if (user.getId() != 119970632)
//            return;

        if (!text.substring(0, 1).equals("/"))
            text = "/" + text;

        System.out.println(text);
        System.out.println(user);
        System.out.println(chatId);

        if (text.length() > 9)
            if (text.toLowerCase().substring(0, 10).equals("/newgame @")) {
                Set<String> playersSet =
                        new HashSet<>(
                                Arrays.asList(text.toLowerCase()
                                        .replace(" ", "")
                                        .substring(text.indexOf("@")).split("@")
                                )
                        );

                int errors = 0;
                for (String player : playersSet) {
                    if (!usersList.allUsers.containsKey(player)) {
                        sendSimpleMessage("User @" + player + " is not registered. Please send me /start in private message", chatId);
                        errors++;
                    }
                }
                if (errors > 0)
                    return;

                cheeseGame = new CheeseGame(playersSet);
                sendTable(chatId);
            }
        }
    }

    private void sendTable(long chatId) {
        cheeseGame.setDie();
        boolean isOnTable = false;
        for (CheeseCard card : cheeseGame.getTable())
            if (card.getValue() == cheeseGame.getDie()) {
                isOnTable = true;
                break;
            }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        SendMessage message = new SendMessage().setChatId(chatId);
        if (isOnTable) {
            List<InlineKeyboardButton> buttonsGet = new ArrayList<>();
            List<InlineKeyboardButton> buttonsThrow = new ArrayList<>();
            for (int i = 0; i < cheeseGame.getTable().size(); i++) {
                buttonsThrow.add(new InlineKeyboardButton()
                        .setText(String.valueOf(cheeseGame.getTable().get(i).getValue()))
                        .setCallbackData("throw" + i)
                );
                buttonsGet.add(new InlineKeyboardButton()
                        .setText(String.valueOf(cheeseGame.getTable().get(i).getValue()))
                        .setCallbackData("get__" + i)
                );
            }
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(buttonsThrow);
            rowList.add(buttonsGet);
            inlineKeyboardMarkup.setKeyboard(rowList);
            message.setReplyMarkup(inlineKeyboardMarkup).setText("Turn of @" + cheeseGame.getCurrentPlayer() + "!\n" + cheeseGame.getDie() + " fell on a die!\nGet or Throw one card!");
        } else {
            List<InlineKeyboardButton> buttonsCheck = new ArrayList<>();
            for (int i = 0; i < cheeseGame.getTable().size(); i++) {
                buttonsCheck.add(new InlineKeyboardButton()
                        .setText(String.valueOf(cheeseGame.getTable().get(i).getValue()))
                        .setCallbackData("check" + i)
                );
            }
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(buttonsCheck);
            inlineKeyboardMarkup.setKeyboard(rowList);
            message.setReplyMarkup(inlineKeyboardMarkup).setText("Turn of @" + cheeseGame.getCurrentPlayer() + "!\n" + cheeseGame.getDie() + " fell on a die!\nCheck one card!");
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendSimpleMessage(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "CodeNamesBot";
    }

    @Override
    public String getBotToken() {
        return token;
    }

}
