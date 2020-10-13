package me.murrobby.igsq.spigot.security;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.murrobby.igsq.spigot.Spigot;

public class EntityPickupItemEvent_Security implements Listener
{
	public EntityPickupItemEvent_Security(Spigot plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void EntityPickupItem_Security(org.bukkit.event.entity.EntityPickupItemEvent event) 
	{
		if(!event.isCancelled()) 
		{
			if(event.getEntity() instanceof Player) 
			{
				Player player = (Player) event.getEntity();
				if (Common_Security.SecurityProtectionQuery(player)) event.setCancelled(true);
			}
		}
	}
	
}
