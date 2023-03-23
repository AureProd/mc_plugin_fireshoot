package fr.aureprod.fireshoot;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Listeners implements Listener 
{
	private Main main;
	
	public Listeners(Main mainbis)
	{
		this.main = mainbis;
	}
	
	public Boolean isIn(Location loc1, Location loc2, Location loc)
	{
		Location locmin = new Location(loc.getWorld(), 0, 0, 0);
		Location locmax = new Location(loc.getWorld(), 0, 0, 0);
		
		if(loc1.getX() > loc2.getX())
		{
			locmin.setX(loc2.getX());
			locmax.setX(loc1.getX());
		}
		else 
		{
			locmin.setX(loc1.getX());
			locmax.setX(loc2.getX());
		}
		
		if(loc1.getY() > loc2.getY())
		{
			locmin.setY(loc2.getY());
			locmax.setY(loc1.getY());
		}
		else 
		{
			locmin.setY(loc1.getY());
			locmax.setY(loc2.getY());
		}
		
		if(loc1.getZ() > loc2.getZ())
		{
			locmin.setZ(loc2.getZ());
			locmax.setZ(loc1.getZ());
		}
		else 
		{
			locmin.setZ(loc1.getZ());
			locmax.setZ(loc2.getZ());
		}
		
		if (loc.getX() < locmax.getX() && 
			loc.getX() > locmin.getX() && 
			loc.getY() < locmax.getY() && 
			loc.getY() > locmin.getY() && 
			loc.getZ() < locmax.getZ() && 
			loc.getZ() > locmin.getZ()) 
		{
			return true;
		}
		
		return false;
	}
	
	@SuppressWarnings("unused")
	@EventHandler
	public void onMove(PlayerMoveEvent event) 
	{
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		
		for (String nommap : main.getConfig().getConfigurationSection("maps").getKeys(false)) 
		{
			for (String stringbis : main.getConfig().getConfigurationSection("maps." + nommap + ".portes").getKeys(false)) 
			{
				List<Float> xyz1 = main.getConfig().getConfigurationSection("maps." + nommap + ".portes." + stringbis).getFloatList("loc1");
				List<Float> xyz2 = main.getConfig().getConfigurationSection("maps." + nommap + ".portes." + stringbis).getFloatList("loc2");
				
				Location loc1 = new Location(loc.getWorld(), xyz1.get(0), xyz1.get(1), xyz1.get(2));
				Location loc2 = new Location(loc.getWorld(), xyz2.get(0), xyz2.get(1), xyz2.get(2));
				
				if (isIn(loc1, loc2, loc)) 
				{	
					Random ran = new Random(); 
					Integer nxtbis = main.getConfig().getConfigurationSection("maps." + nommap + ".spawns").getKeys(false).size();
					Integer nxt = ran.nextInt(nxtbis);
					nxtbis--;
			        
			        for (String stringters : main.getConfig().getConfigurationSection("maps." + nommap + ".spawns").getKeys(false)) 
					{
						if (nxtbis == nxt) 
						{
							List<Float> xyzspawn = main.getConfig().getConfigurationSection("maps." + nommap + ".spawns").getFloatList(stringters);
							
							Location locspawn = new Location(loc.getWorld(), xyzspawn.get(0), xyzspawn.get(1)+1, xyzspawn.get(2));
							
							player.teleport(locspawn);
							
							return;
						}
						else nxtbis--;
					}
				}
			}	
		}	
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) throws InterruptedException 
	{
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		
		for (String nommap : main.getConfig().getConfigurationSection("maps").getKeys(false)) 
		{
			List<Float> xyz1 = main.getConfig().getConfigurationSection("maps." + nommap + ".zone").getFloatList("loc1");
			List<Float> xyz2 = main.getConfig().getConfigurationSection("maps." + nommap + ".zone").getFloatList("loc2");
			
			Location loc1 = new Location(loc.getWorld(), xyz1.get(0), xyz1.get(1), xyz1.get(2));
			Location loc2 = new Location(loc.getWorld(), xyz2.get(0), xyz2.get(1), xyz2.get(2));
			
			List<Float> xyzspawn = main.getConfig().getConfigurationSection("maps." + nommap).getFloatList("spawnpoint");
			
			Location spawnloc = new Location(loc.getWorld(), xyzspawn.get(0), xyzspawn.get(1), xyzspawn.get(2), xyzspawn.get(3), xyzspawn.get(4));
			
			if (isIn(loc1, loc2, loc)) 
			{
				event.setRespawnLocation(spawnloc);
				
				float seconds = (float) 0.05;
				 
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() 
				{
				     public void run() 
				     {
				    	 player.teleport(spawnloc);
				     }
				}, (long)(seconds * 20)); // Always multiply by twenty because that's the amount of ticks in Minecraft
			}
		}
	}
	
	
}
