package me.murrobby.igsq.spigot.expert;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.murrobby.igsq.spigot.Common_Spigot;
import me.murrobby.igsq.spigot.Main_Spigot;


public class SlimeSplitEvent_Expert implements Listener
{
	public SlimeSplitEvent_Expert(Main_Spigot plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void SlimeSplit_Expert(org.bukkit.event.entity.SlimeSplitEvent event) 
	{
		if(Common_Expert.ExpertCheck() && (!event.isCancelled())) 
		{
			if(event.getEntity().getType() == EntityType.SLIME) 
			{
				if(event.getEntity().getCustomName() != null && (event.getEntity().getCustomName().equalsIgnoreCase(Common_Spigot.ChatFormatter("&#84FF00Expert King Slime")) || event.getEntity().getCustomName().equalsIgnoreCase(Common_Spigot.ChatFormatter("&#84FF00Expert Warrior Slime")))) 
				{
					event.setCancelled(true);
				}
				if(event.getEntity().getCustomName() != null) 
				{
					event.getEntity().setCustomName(null);
				}
			}
			else if(event.getEntity().getType() == EntityType.MAGMA_CUBE) 
			{
				if(event.getEntity().getCustomName() != null && (event.getEntity().getCustomName().equalsIgnoreCase(Common_Spigot.ChatFormatter("&#84FF00Expert King Magma Slime")) || event.getEntity().getCustomName().equalsIgnoreCase(Common_Spigot.ChatFormatter("&#84FF00Expert Warrior Magma Slime")))) 
				{
					event.setCancelled(true);
				}
				if(event.getEntity().getCustomName() != null) 
				{
					event.getEntity().setCustomName(null);
				}
			}
		}
	}
	
}
