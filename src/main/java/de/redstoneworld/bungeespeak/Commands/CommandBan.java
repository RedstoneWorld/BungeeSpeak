package de.redstoneworld.bungeespeak.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.redstoneworld.bungeespeak.BungeeSpeak;
import de.redstoneworld.bungeespeak.Configuration.Configuration;
import de.redstoneworld.bungeespeak.Configuration.Messages;
import de.redstoneworld.bungeespeak.util.MessageUtil;
import de.redstoneworld.bungeespeak.util.Replacer;
import de.redstoneworld.bungeespeak.AsyncQueryUtils.QueryBan;

import net.md_5.bungee.api.CommandSender;

public class CommandBan extends BungeeSpeakCommand {

	public CommandBan() {
		super("ban");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 2) {
			sendTooFewArgumentsMessage(sender, Messages.MC_COMMAND_BAN_USAGE.get());
			return;
		}

		if (!isConnected(sender)) return;

		Client client = getClient(args[1], sender);
		if (client == null) return;

		String tsMsg = Messages.MC_COMMAND_BAN_TS.get();
		String mcMsg = Messages.MC_COMMAND_BAN_MC.get();
		String msg = Messages.MC_COMMAND_DEFAULT_REASON.get();
		if (args.length > 2) {
			msg = combineSplit(2, args, " ");
		}

		Replacer r = new Replacer().addSender(sender).addTargetClient(client.getMap()).addMessage(msg);
		tsMsg = MessageUtil.toTeamspeak(r.replace(tsMsg), false, Configuration.TS_ALLOW_LINKS.getBoolean());
		mcMsg = r.replace(mcMsg);

		if (tsMsg == null || tsMsg.isEmpty()) return;
		if (tsMsg.length() > TS_MAXLENGHT) {
			String tooLong = Messages.MC_COMMAND_ERROR_MESSAGE_TOO_LONG.get();
			tooLong = new Replacer().addSender(sender).addTargetClient(client.getMap()).replace(tooLong);
			send(sender, Level.WARNING, tooLong);
			return;
		}

		Integer i = Integer.valueOf(client.get("clid"));
		QueryBan qb = new QueryBan(i, tsMsg);
		BungeeSpeak.getInstance().getProxy().getScheduler().runAsync(BungeeSpeak.getInstance(), qb);
		broadcastMessage(mcMsg, sender);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length != 2) return Collections.emptyList();
		List<String> al = new ArrayList<String>();
		for (Client client : BungeeSpeak.getClientList().getClients().values()) {
			String n = client.getNickname().replaceAll(" ", "");
			if (n.toLowerCase().startsWith(args[1].toLowerCase())) {
				al.add(n);
			}
		}
		return al;
	}
}
