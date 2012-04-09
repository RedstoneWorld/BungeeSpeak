package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPm extends BukkitSpeakCommand {
	
	public CommandPm(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 3) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts pm target message");
			return;
		} else if (!ts.getAlive()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		} 
		
		TeamspeakUser user = ts.getUserByPartialName(args[1]);
		if (user == null) {
			send(sender, Level.WARNING, "&4Can't find the user you want to PM.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 2, args.length)) {
			sb.append(s);
			sb.append(" ");
		}
		String SenderName;
		if (sender instanceof Player) {
			SenderName = sender.getName();
		} else {
			SenderName = stringManager.getTeamspeakNickname();
		}
		ts.pushMessage("sendtextmessage targetmode=1" 
				+ " target=" + user.getID()
				+ " msg=" + convert(sb.toString()), SenderName);
		
		String message = stringManager.getMessage("Pm");
		message = message.replaceAll("%target%", user.getName());
		message = message.replaceAll("%msg%", sb.toString());
		
		send(sender, Level.INFO, message);
	}
}