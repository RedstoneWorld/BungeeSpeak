package de.redstoneworld.bungeespeak.AsyncQueryUtils;

import com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException;
import de.redstoneworld.bungeespeak.BungeeSpeak;

public class QueryKick implements Runnable {

	private int id;
	private boolean local;
	private String reason;

	public QueryKick(int clientID, boolean onlyChannelKick, String kickReason) {
		id = clientID;
		local = onlyChannelKick;
		reason = kickReason;
	}

	@Override
	public void run() {
		try {
			if (local) {
				BungeeSpeak.getQuery().getApi().kickClientFromChannel(reason, id);
			} else {
				BungeeSpeak.getQuery().getApi().kickClientFromServer(reason, id);
			}
		} catch (TS3CommandFailedException ex) {
			BungeeSpeak.log().info("kickClient()" + ex.getError().getId() + ex.getError().getMessage() + ex.getError().getExtraMessage() + ex.getError().getFailedPermissionId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
