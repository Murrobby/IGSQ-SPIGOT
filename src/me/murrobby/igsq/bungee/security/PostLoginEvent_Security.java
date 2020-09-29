package me.murrobby.igsq.bungee.security;

import me.murrobby.igsq.bungee.Common_Bungee;
import me.murrobby.igsq.bungee.Database_Bungee;
import me.murrobby.igsq.bungee.Main_Bungee;
import me.murrobby.igsq.shared.Common_Shared;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginEvent_Security implements Listener
{
	public PostLoginEvent_Security(Main_Bungee plugin)
	{
		ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void PostLogin_Security(net.md_5.bungee.api.event.PostLoginEvent event) 
	{
		//Runs on bungee connect complete
		ProxiedPlayer player = event.getPlayer();
		String playerUUID = player.getUniqueId().toString();
		int playerProtocol = player.getPendingConnection().getVersion();
		Integer recomendedProtocol = Integer.parseInt(Common_Bungee.getFieldString("SUPPORT.protocol.recommended", "config"));
		if(playerProtocol == recomendedProtocol) 
		{
			player.sendMessage(TextComponent.fromLegacyText(Common_Bungee.chatFormatter("&#00FF00Your Minecraft is running the recommended version! :)")));
		}
		else if( playerProtocol > recomendedProtocol) 
		{
			player.sendMessage(TextComponent.fromLegacyText(Common_Bungee.chatFormatter("&#ffb900Your Minecraft is running a newer version than the server! Issues may arise.")));
		}
		else 
		{
			player.sendMessage(TextComponent.fromLegacyText(Common_Bungee.chatFormatter("&cYour Minecraft is running a older version than the server! Issues will arise! Most Servers Will Be Inaccessible!")));
		}
		
		if(Database_Bungee.ScalarCommand("SELECT count(*) FROM linked_accounts WHERE uuid = '"+ playerUUID + "' AND current_status = 'Linked';") == 1) player.sendMessage(TextComponent.fromLegacyText(Common_Bungee.chatFormatter("&#00FF00Your account is already Linked! :)")));
		else if(Database_Bungee.ScalarCommand("SELECT count(*) FROM linked_accounts WHERE uuid = '"+ playerUUID + "' AND current_status = 'mwait';") >= 1)
		{
			player.sendMessage(TextComponent.fromLegacyText(Common_Bungee.chatFormatter("&#ffb900Your account has a pending link/s. Do &#FFFF00/link &#ffb900to learn more.")));
			LinkMessage(player);
		}
		else if(Database_Bungee.ScalarCommand("SELECT count(*) FROM linked_accounts WHERE uuid = '"+ playerUUID + "' AND current_status = 'dwait';") >= 1)
		{
			player.sendMessage(TextComponent.fromLegacyText(Common_Bungee.chatFormatter("&#C8C8C8Your account has a pending outbound link/s. Do &#FFFF00.mclink on discord.")));
			LinkMessage(player);
			
		}
		else
		{
			player.sendMessage(TextComponent.fromLegacyText(Common_Bungee.chatFormatter("&#CD0000No Pending Account Links Found. &#FFFF00You can link your account to discord with /link add [discord_username/id]")));
			LinkMessage(player);
		}
		
		
		String currentStatus = Common_Bungee.getFieldString(playerUUID + ".discord.2fa.status", "player");
		String id = Common_Shared.removeNull(Common_Bungee.getFieldString(playerUUID + ".discord.id", "player"));
		if((!id.equalsIgnoreCase(""))  && (currentStatus == null || currentStatus.equalsIgnoreCase(""))) player.sendMessage(TextComponent.fromLegacyText(Common_Bungee.chatFormatter("&#a900ffYou can enable 2FA for increased account security! &#FFFF00 See /2fa for more details.")));
	}
	
	private void LinkMessage(ProxiedPlayer player) 
	{
		player.sendMessage(TextComponent.fromLegacyText(Common_Bungee.chatFormatter("&#00b7ffIf you want your rank portrayed on discord and not &#006e99[&#00b7ffSquirrel&#006e99] &#00b7ffyou will have to link your account.\n&#C0C0C0Linking also unlocks the ability to use our 2FA security feature.")));
	}
	
}