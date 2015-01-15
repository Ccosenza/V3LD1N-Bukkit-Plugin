package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.StringUtil;

public class FAQCommand extends V3LD1NCommand {
    public FAQCommand() {
        this.addUsage("", "Send a list of questions");
        this.addUsage("<question number>", "Send the answer to the question");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String border = StringUtil.jsonMessage(Message.FAQ_BORDER.toString(), "dark_red");
            String top = StringUtil.jsonMessage(Message.FAQ_TOP.toString(), "red");
            if (args.length == 0) {
                ChatUtil.sendJsonMessage(p, border, 0);
                ChatUtil.sendJsonMessage(p, top, 0);
                ChatUtil.sendJsonMessage(p, border, 0);
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
                ChatUtil.sendJsonMessage(p, StringUtil.jsonMessage(Message.FAQ_HELP.toString(), "green"), 0);
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
                    ChatUtil.sendJsonMessage(p, border, 0);
                    ChatUtil.sendJsonMessage(p, top, 0);
                    ChatUtil.sendJsonMessage(p, border, 0);
                    ChatUtil.sendJsonMessage(p, question, 0);
                    ChatUtil.sendJsonMessage(p, answer, 0);
                    ChatUtil.sendJsonMessage(p, back, 0);
                    return true;
                }
                p.sendMessage(Message.FAQ_INVALID_QUESTION.toString());
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}