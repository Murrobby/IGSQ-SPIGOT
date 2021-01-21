package me.murrobby.igsq.spigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitScheduler;

import me.murrobby.igsq.spigot.expert.Main_Expert;
import me.murrobby.igsq.spigot.lp.Main_LP;
import me.murrobby.igsq.spigot.main.AsyncPlayerChatEvent_Main;
import me.murrobby.igsq.spigot.main.EntityDamageEvent_Main;
import me.murrobby.igsq.spigot.main.EntityDeathEvent_Main;
import me.murrobby.igsq.spigot.main.InventoryClickEvent_Main;
import me.murrobby.igsq.spigot.main.PlayerCommandPreprocessEvent_Main;
import me.murrobby.igsq.spigot.main.PlayerJoinEvent_Main;
import me.murrobby.igsq.spigot.main.PlayerQuitEvent_Main;
import me.murrobby.igsq.spigot.security.Main_Security;
import me.murrobby.igsq.spigot.blockhunt.Main_BlockHunt;
import me.murrobby.igsq.spigot.commands.Main_Command;
import me.murrobby.igsq.spigot.commands.Team_Command;

public class Spigot extends JavaPlugin implements PluginMessageListener{
	public BukkitScheduler scheduler = getServer().getScheduler();
	@Override
	public void onEnable()
	{ 
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "igsq:yml", this);
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "igsq:sound", this);
		scheduler.scheduleSyncRepeatingTask(this, new Runnable()
		{

			@Override
			public void run() 
			{
					Yaml.saveFileChanges("@all");
					Yaml.loadFile("@all");
			} 		
    	}, 20, 600);
		
		new Database(this);
		
		
		new PlayerJoinEvent_Main();
		new InventoryClickEvent_Main();
		new PlayerCommandPreprocessEvent_Main();
		new EntityDeathEvent_Main();
		new AsyncPlayerChatEvent_Main();
		new EntityDamageEvent_Main();
		new PlayerQuitEvent_Main();
		
		new Main_Expert();
		new Main_Security();
		new Main_Command();
		new Team_Command();
		/*
		if(this.getServer().getPluginManager().getPlugin("NametagEdit") != null && Yaml.getFieldBool("SUPPORT.nametagedit", "config")) 
		{
			System.out.println("NametagEdit Hook in Luckperms Module Enabled.");
			nametagEdit = true;
		}
		else System.out.println("NametagEdit Hook in Luckperms Module Disabled.");
		*/
		if(this.getServer().getPluginManager().getPlugin("LuckPerms") != null && YamlWrapper.isLuckpermsSupported()) 
		{
			System.out.println("Luckperms Module Enabled.");
			new Main_LP(this);
			YamlWrapper.setDefaultChatController("mainlp");
			YamlWrapper.setDefaultNameController("main");
		}
		else 
		{
			System.out.println("Luckperms Module Disabled.");
			YamlWrapper.setDefaultChatController("main");
			YamlWrapper.setDefaultNameController("none");
		}
		if(YamlWrapper.isBlockHunt()) 
		{
			System.out.println("BlockHunt enabled.");
			new Main_BlockHunt();
		}
		else 
		{
			System.out.println("BlockHunt disabled!");
		}
	}

	public void onLoad()
	{
		Common.spigot = this;
		Yaml.createFiles();
		Yaml.loadFile("@all");
		YamlWrapper.applyDefault();
		for(Player player : Bukkit.getOnlinePlayers()) Communication.setDefaultTagData(player);
	}
	
	public void onDisable()
	{
		for(Player player : Bukkit.getOnlinePlayers()) Communication.deletePlayer(player);
		BlockCluster.cleanup();
		this.getServer().getScheduler().cancelTasks(this);
		Yaml.saveFileChanges("@all");
		Yaml.disgardAndCloseFile("@all");
		this.getServer().getPluginManager().disablePlugin(this);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		Communication.onPluginMessageReceived(channel, player, message);
	}
	
}
