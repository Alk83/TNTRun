package tntrun.arena;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import tntrun.TNTRun;

public class PlayerHandler {

	private TNTRun plugin;
	private Arena arena;
	public PlayerHandler(TNTRun plugin, Arena arena)
	{
		this.plugin = plugin;
		this.arena = arena;
	}
	
	//spawn player on arena
	public void spawnPlayer(Player player, String msgtoplayer, String msgtoarenaplayers)
	{
		//change player status
		player.setGameMode(GameMode.SURVIVAL);
		player.setFlying(false);
		player.setAllowFlight(false);
		plugin.pdata.storePlayerLocation(player.getName());
		plugin.pdata.storePlayerInventory(player.getName());
		plugin.pdata.storePlayerArmor(player.getName());
		player.teleport(arena.getSpawnPoint());
		//send message to player
		if (!msgtoplayer.equalsIgnoreCase(""))
		{
			player.sendMessage(msgtoplayer);
		}
		//send message to other players
		if (!msgtoplayer.equalsIgnoreCase(""))
		{
			for (String p : plugin.pdata.getArenaPlayers(arena))
			{
				Bukkit.getPlayerExact(p).sendMessage(msgtoarenaplayers);
			}
		}
		//set player on arena data
		plugin.pdata.setPlayerArena(player.getName(), arena);
		//check for game start
		if (plugin.pdata.getArenaPlayers(arena).size() == arena.getMaxPlayers() || plugin.pdata.getArenaPlayers(arena).size() == arena.getMinPlayers())
		{
			arena.arenagh.runArena();
		}
	}
	//remove player from arena
	public void leavePlayer(Player player, String msgtoplayer, String msgtoarenaplayers)
	{
		//remove player on arena data
		plugin.pdata.removePlayerFromArena(player.getName());
		//restore player status
		plugin.pdata.restorePlayerLocation(player.getName());
		plugin.pdata.restorePlayerInventory(player.getName());
		plugin.pdata.restorePlayerArmor(player.getName());
		//send message to player
		if (!msgtoplayer.equalsIgnoreCase(""))
		{
			player.sendMessage(msgtoplayer);
		}
		//send message to other players
		if (!msgtoarenaplayers.equalsIgnoreCase(""))
		{
			for (String p : plugin.pdata.getArenaPlayers(arena))
			{
				Bukkit.getPlayerExact(p).sendMessage(msgtoarenaplayers);
			}
		}
		
		votes.remove(player.getName());
	}
	//vote for game start
	private HashSet<String> votes = new HashSet<String>();
	public boolean vote(Player player)
	{
		if (!votes.contains(player.getName()))
		{
			votes.add(player.getName());
			if (votes.size() >= ((int)plugin.pdata.getArenaPlayers(arena).size()*arena.getVotePercent()))
			{
				arena.arenagh.runArena();
			}	
			return true;
		}
		return false;
	}
	
	
}
