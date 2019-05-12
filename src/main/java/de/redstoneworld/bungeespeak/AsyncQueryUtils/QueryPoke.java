package de.redstoneworld.bungeespeak.AsyncQueryUtils;

import com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException;
import de.redstoneworld.bungeespeak.BungeeSpeak;

public class QueryPoke implements Runnable {

	private int id;
	private String msg;

	public QueryPoke(int clientID, String message) {
		id = clientID;
		msg = message;
	}

	@Override
	public void run() {
		try {
			BungeeSpeak.getQuery().getApi().pokeClient(id, msg);
		} catch (TS3CommandFailedException ex) {
			BungeeSpeak.log().info("pokeClient()" + ex.getError().getId() + ex.getError().getMessage() + ex.getError().getExtraMessage() + ex.getError().getFailedPermissionId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
