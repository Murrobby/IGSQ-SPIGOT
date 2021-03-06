package me.murrobby.igsq.spigot.security;

import me.murrobby.igsq.spigot.Common_Spigot;
import me.murrobby.igsq.spigot.Database_Spigot;
import me.murrobby.igsq.spigot.Main_Spigot;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DiscordLink_Security
{	
	Main_Spigot plugin;
	int twofaTask = -1;
	final int taskID;
	
	public DiscordLink_Security(Main_Spigot plugin,int taskID) 
	{
		this.plugin = plugin;
		this.taskID = taskID;
		DiscordLinkQuery();
	}
	private void DiscordLinkQuery() 
	{
		twofaTask = plugin.scheduler.scheduleSyncRepeatingTask(plugin, new Runnable()
    	{

			@Override
			public void run() 
			{
				DiscordLink();
				if(Main_Security.taskID != taskID) 
				{
					plugin.scheduler.cancelTask(twofaTask);
					System.out.println("Task: \"Discord Link Security\" Expired Closing Task To Save Resources.");
				}
			} 		
    	}, 0, 200);
	}
	private void DiscordLink() 
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if(Database_Spigot.ScalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE uuid = '" + player.getUniqueId().toString() + "' AND current_status = 'linked';") == 1) 
			{
				ResultSet discord_accounts = Database_Spigot.QueryCommand("SELECT * FROM discord_accounts WHERE id = (SELECT id FROM linked_accounts WHERE uuid = '" +  player.getUniqueId().toString() +"');");
				try
				{
					discord_accounts.next();
					Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.id",discord_accounts.getString(1));
					Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.username",discord_accounts.getString(2));
					Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.nickname",discord_accounts.getString(3));
					Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.role",discord_accounts.getString(4));
					Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.founder",discord_accounts.getBoolean(5));
					Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.birthday",discord_accounts.getBoolean(6));
					Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.nitroboost",discord_accounts.getBoolean(7));
					Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.supporter",discord_accounts.getBoolean(8));
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else 
			{
				Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.id","");
				Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.username","");
				Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.nickname","");
				Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.role","default");
				Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.founder",false);
				Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.birthday",false);
				Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.nitroboost",false);
				Common_Spigot.playerData.set(player.getUniqueId().toString() + ".discord.supporter",false);
			}
		}
		try 
		{
			Common_Spigot.playerData.save(Common_Spigot.playerDataFile);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
