import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class MHcmdListener extends PluginListener {
	private static Logger log = Logger.getLogger("Minecraft");
	private static DecimalFormat df = new DecimalFormat("#########0.00");
	SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");

	@SuppressWarnings("unused")
	private Plugin plugin = null;

	static String[][] commands = { { "Home2", "Teleports your to home2" },
			{ "SetHome2", "Sets your home2" } };

	@SuppressWarnings("unused")
	private boolean doCommandHome2(Player sender, String[] cmd) {
		try {
			Warp home;
			if (cmd.length > 1 && sender.isAdmin()) {
				home = etc.getDataSource().getHome(cmd[1] + "#2");
			} else
				home = etc.getDataSource().getHome(sender.getName() + "#2");

			if (sender.getWorld().getType() != World.Type.NORMAL)
				if (sender.canIgnoreRestrictions())
					sender.switchWorlds();
				else {
					sender.notify("The veil between the worlds keeps you in the Nether...");
					return true;
				}

			if (home != null) {
				sender.teleportTo(home.Location);
			} else if (cmd.length > 1 && sender.isAdmin()) {
				sender.notify(cmd[1] + "'s home 2 does not exists");
			} else {
				sender.teleportTo(sender.getWorld().getSpawnLocation());
			}

		} catch (Exception e) {
			log.warning("Error /home2 command");
		}
		return true;
	}

	@SuppressWarnings("unused")
	private boolean doCommandSetHome2(Player sender, String[] cmd) {
		try {
			Player player = sender;
			Warp home = new Warp();
			if (cmd.length > 1 && sender.isAdmin()) {
				player = etc.getServer().matchPlayer(cmd[1]);
				if (player == null) {
				sender.notify("Could not find player "+cmd[1]);
				return true;
				}
			}
			if ((sender.getWorld().getType().getId() != 0)) {
				sender.notify("You cannot set a home in the Nether, mortal.");
				return true;
			}
			home.Location = sender.getLocation();
			home.Group = "";
			home.Name = player.getName()+"#2";
			etc.getInstance().changeHome(home);
			if (player == sender) {
				sender.notify("Your home2 has been set.");
			} else {
				sender.notify(player.getName()+"'s home2 has been set.");
			}
		} catch (Exception e) {
			log.warning("Error /sethome2 command");
		}
		return true;
	}

	private boolean invoke(String c, Player sender, String[] cmd) {
		String aMethod = "doCommand" + c;
		@SuppressWarnings("rawtypes")
		Class[] params = new Class[] { Player.class, String[].class };
		Object[] args = new Object[] { sender, cmd };
		Method m;
		try {
			m = this.getClass().getDeclaredMethod(aMethod, params);
			Boolean r = (Boolean) m.invoke(this, args);
			return r.booleanValue();
		} catch (Exception e) {
			log.severe("MultiHomes: Can't invoke command "+c);
			return false;
		}
	}

	@SuppressWarnings("unused")
	private void sendData(Player user, String caption, String data) {
		user.sendMessage(Colors.LightGreen + caption + Colors.Gold + data);
	}

	public boolean onCommand(Player sender, String[] cmd) {
		for (String[] command : commands) {
			if (cmd[0].equalsIgnoreCase("/" + command[0])
					&& (sender.canUseCommand("/multihomes") || sender
							.isAdmin())) {
				return invoke(command[0], sender, cmd);
			}
		}
		return false;
	}
}
