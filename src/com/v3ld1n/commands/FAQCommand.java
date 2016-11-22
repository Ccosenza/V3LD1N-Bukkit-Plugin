package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.MessageType;
import com.v3ld1n.util.StringUtil;

public class FAQCommand extends V3LD1NCommand {
    private final int PAGE_SIZE = 10;

    public FAQCommand() {
        this.addUsage("[page]", "Display the list of questions");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sendPlayerMessage(sender);
            return true;
        }
        Player player = (Player) sender;
        int page;

        if (args.length == 0) {
            // Display the first page of the FAQ
            page = 1;
            displayFAQ(player, page);
            return true;
        } else if (args.length == 1 && StringUtil.isInteger(args[0])) {
            // Display a page of the FAQ
            page = StringUtil.toInteger(args[0], 1);
            displayFAQ(player, page);
            return true;
        }
        this.sendUsage(player);
        return true;
    }

    private void displayFAQ(Player p, int page) {
        List<FAQ> questions = new ArrayList<>(V3LD1N.getQuestions());
        Message.FAQ_BORDER_TOP.sendF(p, page, ChatUtil.getNumberOfPages(questions, PAGE_SIZE));
        List<FAQ> questionsOnPage = ChatUtil.getPage(questions, page, PAGE_SIZE);

        for (FAQ question : questionsOnPage) {
            List<String> answer = question.getAnswer();
            try {
                String message = "{\"text\":\"" + question.getQuestion() + "\","
                        + "\"color\":\"gold\","
                        + "\"hoverEvent\":{"
                        + "\"action\":\"show_text\","
                        + "\"value\":\"%s\"}}";

                StringBuilder builder = new StringBuilder();
                builder.append(Message.FAQ_QUESTION.toString() + question.getQuestion() + "\n");
                builder.append(Message.FAQ_ANSWER.toString() + Message.FAQ_ANSWER_COLOR + answer.get(0));
                for (String answerLine : answer) {
                	if (answer.indexOf(answerLine) == 0) continue;
                	builder.append("\n" + Message.FAQ_ANSWER_COLOR.toString() + answerLine);
                }

                String builderString = builder.toString();
                message = String.format(message, builderString);
                ChatUtil.sendJsonMessage(p, message, MessageType.CHAT);
            } catch (Exception e) {
                Message.FAQ_ERROR.send(p);
                e.printStackTrace();
            }
        }
        Message.FAQ_HELP.send(p);
        Message.FAQ_BORDER_BOTTOM.send(p);
    }
}