package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;

public class FAQCommand extends V3LD1NCommand {
    public FAQCommand() {
        this.addUsage("", "Send a list of questions");
        this.addUsage("<question number>", "Send the answer to the question");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                Message.FAQ_BORDER_TOP.send(p);
                for (FAQ faq : V3LD1N.getQuestions()) {
                    ChatUtil.sendJsonMessage(p,
                    "{text:\"" + faq.getName() + "\","
                    + "color:" + faq.getNameColor() + ","
                    + "extra:["
                    + "{text:\"" + faq.getQuestion() + "\","
                    + "color:" + faq.getQuestionColor() + ","
                    + "clickEvent:{"
                    + "action:\"run_command\","
                    + "value:\"/" + label + " " + faq.getId() + "\"}}]}"
                    , 0);
                }
                Message.FAQ_HELP.send(p);
                Message.FAQ_BORDER_BOTTOM.send(p);
                return true;
            } else if (args.length == 1) {
                int arg;
                try {
                    arg = Integer.parseInt(args[0]);
                } catch (IllegalArgumentException e) {
                    return false;
                }
                if (arg <= V3LD1N.getQuestions().size() && arg > 0) {
                    FAQ faq = V3LD1N.getQuestions().get(arg - 1);
                    String question = "{text:\"" + Message.FAQ_QUESTION + "\","
                            + "color:gold,"
                            + "extra:["
                            + "{text:\"" + faq.getQuestion() + "\","
                            + "color:" + faq.getQuestionColor() + "}]}";
                    String answer = "{text:\"" + Message.FAQ_ANSWER + "\","
                            + "color:yellow,"
                            + "extra:["
                            + "{text:\"" + faq.getAnswer() + "\","
                            + "color:" + faq.getAnswerColor() + "}]}";
                    String back = "{text:\"" + Message.FAQ_BACK + "\","
                            + "color:" + ConfigSetting.FAQ_BACK_COLOR.getString() + ","
                            + "clickEvent:{"
                            + "action:\"run_command\","
                            + "value:\"/" + label + "\"}}";
                    Message.FAQ_BORDER_TOP.send(p);
                    ChatUtil.sendJsonMessage(p, question, 0);
                    ChatUtil.sendJsonMessage(p, answer, 0);
                    ChatUtil.sendJsonMessage(p, back, 0);
                    Message.FAQ_BORDER_BOTTOM.send(p);
                    return true;
                }
                Message.FAQ_INVALID_QUESTION.send(p);
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sendPlayerMessage(sender);
        return true;
    }
}