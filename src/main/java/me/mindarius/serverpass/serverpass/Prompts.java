package me.mindarius.serverpass.serverpass;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class Prompts {
    public static class TestPass implements Prompt {
        @Override public String getPromptText(ConversationContext context) { return "Please enter the server password in chat.\nEntering it correctly will return you to survival mode, and you will not be asked again.\nIf you answer incorrectly, you will be banned until you appeal to a server admin.\nIf you do not know the password, disconnect without entering anything in chat to avoid being banned."; }
        @Override public boolean blocksForInput(ConversationContext context) { return true; }
        @Override public Prompt acceptInput(ConversationContext context, String input) {
            Player p = (Player) context.getForWhom();
            if(input.equals(Main.pass)){
                Main.approvedPlayers.add(p.getUniqueId());
                p.setGameMode(GameMode.SURVIVAL);
                p.setFlySpeed(.1F);
            } else {
                Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), "Incorrect password", null, "ServerPass, automated server protection");
                p.kickPlayer("You have been automatically banned by ServerPass for supplying an incorrect server password. Contact server administration to remove this ban.");
            }
            return END_OF_CONVERSATION;
        }
    }
    public static class PassSet implements Prompt {

        @Override public String getPromptText(ConversationContext context) {
            if(Main.pass==null){
                return "Please enter your desired server password into chat. You will be prompted to confirm this password in case of typos.\nNote: The password may not begin with '/', and must obey other minecraft chat restrictions.";
            } else {
                return "Is \""+Main.pass+"\" your desired password? Re-enter the password to confirm. Entering anything else will ask for a new password.";
            }
        }

        @Override public boolean blocksForInput(ConversationContext context) { return true; }

        @Override public Prompt acceptInput(ConversationContext context, String input) {
            Player p = (Player) context.getForWhom();
            if(Main.pass==null){
                Main.pass = input;
                return this;
            } else {
                if(input.equals(Main.pass)){
                    return END_OF_CONVERSATION;
                } else {
                    Main.pass = null;
                    return this;
                }
            }
        }
    }
}
