package net.mchel.plugin.whois;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Chelcy
 *         2017/05/10
 */
public class Whois extends JavaPlugin {

	private String prefix;
	private String ver;

	@Override
	public void onEnable() {
		prefix = ChatColor.BOLD + " " + ChatColor.RED + "Whois " + ChatColor.RESET + ChatColor.LIGHT_PURPLE + ">" + ChatColor.RED + "> " + ChatColor.RESET;
		ver = Bukkit.getServer().getClass().getPackage().getName();
		ver = ver.substring(ver.lastIndexOf(".") + 1);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(prefix + "/whois <player>");
			return true;
		}
		String targetName = args[0];
		Player targetPlayer = Bukkit.getPlayer(targetName);
		if (targetPlayer == null) {
			sender.sendMessage(prefix + "プレイヤー " + args[0] + " はオンラインではありません。");
			return true;
		}
		Location loc = targetPlayer.getLocation();
		String[] str = {
				ChatColor.LIGHT_PURPLE + " ------------|" + ChatColor.YELLOW + " Player whois " + ChatColor.LIGHT_PURPLE + "|------------",
				"   Player: " + targetPlayer.getName(),
				"   UUID: " + targetPlayer.getUniqueId().toString(),
				"   Address: " + replaceLast(targetPlayer.getAddress().toString().replaceFirst("/", "").replace(String.valueOf(targetPlayer.getAddress().getPort()), ""), ":", ""),
				"   Ping: " + getPing(targetPlayer),
				"   World: " + loc.getWorld().getName(),
				"   Location: " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ(),
				"   Gamemode: " + targetPlayer.getGameMode().toString(),
				"   Flymode: " + (targetPlayer.getAllowFlight()? "ON": "OFF"),
				ChatColor.LIGHT_PURPLE + " -------------------------------------"
		};
		sender.sendMessage(str);
		return true;
	}

	private String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	}

	private int getPing(Player player) {
		int ping = -1;
		try {
			Class<?> cp = Class.forName("org.bukkit.craftbukkit."+ ver +".entity.CraftPlayer");
			Object cpc = cp.cast(player);
			Method m = cpc.getClass().getMethod("getHandle");
			Object o = m.invoke(cpc);
			Field f = o.getClass().getField("ping");
			ping = f.getInt(o);
		} catch (Exception e) {
			Bukkit.getLogger().warning(e.getLocalizedMessage());
		}
		return ping;
	}
}
