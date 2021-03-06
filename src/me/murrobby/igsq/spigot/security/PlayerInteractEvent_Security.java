package me.murrobby.igsq.spigot.security;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.murrobby.igsq.spigot.Main_Spigot;

public class PlayerInteractEvent_Security implements Listener
{
	public PlayerInteractEvent_Security(Main_Spigot plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void PlayerInteract_Security(org.bukkit.event.player.PlayerInteractEvent event) 
	{
		String message = "Interact";
		if(!event.getMaterial().isAir()) message += " With " + event.getMaterial();
		if(event.getClickedBlock() != null) message += " On " + event.getClickedBlock().getType();
		if (Common_Security.SecurityProtection(event.getPlayer(),message)) event.setCancelled(true);
	}
	
}
