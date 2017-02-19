package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.StringUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
        Message.get("faq-border-top").sendF(p, page, ChatUtil.getNumberOfPages(questions, PAGE_SIZE));
        List<FAQ> questionsOnPage = ChatUtil.getPage(questions, page, PAGE_SIZE);

        for (FAQ question : questionsOnPage) {
            List<String> answer = question.getAnswer();
            try {
                StringBuilder builder = new StringBuilder();
                builder.append(Message.get("faq-question").toString() + question.getQuestion() + "\n");
                builder.append(Message.get("faq-answer").toString() + Message.get("faq-answer-color") + answer.get(0));
                for (String answerLine : answer) {
                	if (answer.indexOf(answerLine) == 0) continue;
                	builder.append("\n" + Message.get("faq-answer-color").toString() + answerLine);
                }

                String builderString = builder.toString();

                TextComponent message = new TextComponent(question.getQuestion());
                message.setColor(ChatColor.GOLD);
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(builderString).create()));
                p.spigot().sendMessage(message);
            } catch (Exception e) {
            	Message.get("faq-display-error").send(p);
                e.printStackTrace();
            }
        }
        Message.get("faq-help").send(p);
        Message.get("faq-border-bottom").send(p);
    }
}