package fr.aureprod.fireshoot;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin 
{
	@Override
	public void onEnable() 
	{
		saveDefaultConfig();
		
		System.out.println("Le plugin FireShoot est est demarre !!!");
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
	}
	
	@Override
	public void onDisable() 
	{
		System.out.println("Le plugin FireShoot est arreter !!!");
	}
}
