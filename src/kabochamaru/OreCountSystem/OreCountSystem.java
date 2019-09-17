package kabochamaru.OreCountSystem;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;



public class OreCountSystem extends JavaPlugin implements Listener{
	private static final String SIDEBAR_NAME = "countsidebar";
	public Scoreboard scoreboard;
    public static Objective objective;
    public boolean status = false;
    public static int time = 30;
    
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("プラグインが有効になりました");
		scoreboard = getServer().getScoreboardManager().getMainScoreboard();
		objective = scoreboard.registerNewObjective(SIDEBAR_NAME, "test");
		objective.setDisplayName("§a鉱石カウンター");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public void onDisable() {
        objective.unregister();
    }
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		setScore(name, 0);
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		removeScore(name);
	}
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(status == true && time > 0) {
			Material bname = event.getBlock().getType();
			if(bname == Material.DIAMOND_ORE) {
				Player player = event.getPlayer();
				String name = player.getName();	
		        int point = getScore(name);
		        setScore(name, point + 1);
		   }
		}
	}
	public class Main extends JavaPlugin {
	    @Override
	    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
	        if (!command.getName().equalsIgnoreCase("oc")) return super.onTabComplete(sender, command, alias, args);
	        if (args.length == 1) {
	            if (args[0].length() == 0) { 
	                return Arrays.asList("start", "timeset", "stop" , "reset" );
	            } else {
	                if ("start".startsWith(args[0])) {
	                    return Collections.singletonList("start");
	                } else if ("timeset".startsWith(args[0])) {
	                    return Collections.singletonList("timeset");
	                } else if ("stop".startsWith(args[0])) {
	                	return Collections.singletonList("stop");
	                } else if ("reset".startsWith(args[0])) {
	                	return Collections.singletonList("reset");
	                }
	            }
	        }
	        return super.onTabComplete(sender, command, alias, args);
	    }
	}
	public boolean onCommand(CommandSender sender, Command command, String Label, String[] args){	
		switch(command.getName()){
		case "oc":
			try {
				if (args[0] != null) {
					switch (args[0]) {
					case "start":
						try{
							if(status == true) {
								sender.sendMessage("§f>>>§b[INFO]§fゲームはすでに開始されてます");	
							}else {
								status = true;
								int times = time * 2000;
								timer.startTimer(times);
								for(Player player : getServer().getOnlinePlayers()) {
									addSoubi(player);
								}
								Bukkit.broadcastMessage("§f>>>§b[INFO]§fゲームが§a開始§fされました");
							}
						}
						catch(ArrayIndexOutOfBoundsException e){
							sender.sendMessage("§f>>>/oc start");	
						}
						break;
						
					case "timeset":
						try {
							if (args[1] != null) {
								if(status == false) {
									time = Integer.parseInt(args[1]);
									sender.sendMessage("§f>>>§b[INFO]§fタイマーを§a"+args[1]+"§f分に変更しました");
								}else{
									sender.sendMessage("§f>>>§b[INFO]§fゲーム中に変更はできません");
								}
							}
						}
						catch(ArrayIndexOutOfBoundsException e){
							sender.sendMessage("§f>>>/oc timeset [time]");
					}		
						break;
					case "stop":
						try {
							if(status == false) {
								sender.sendMessage("§f>>>§b[INFO]§fゲームはまだ開始されてません");
							}else {
								status = false;
								timer.stopTimer();
								objective.setDisplayName("§a鉱石カウンター");
								Bukkit.broadcastMessage("§f>>>§b[INFO]§fゲームが§a終了§fしました");
							}
						}
						catch(ArrayIndexOutOfBoundsException e){
							sender.sendMessage("§f>>>/oe timeset [time]");
					}
						break;
					case "reset":
						try {
								for(Player player : getServer().getOnlinePlayers()) {
									String name = player.getName();
									setScore(name, 0);
								}
								Bukkit.broadcastMessage("§f>>>§b[INFO]カウントをリセットしました");
						}
						catch(ArrayIndexOutOfBoundsException e){
							sender.sendMessage("§f>>>/oe reset");
					}
						break;
					case "setcount":
						try {
							if (args[1] != null) {
								Player player = getServer().getPlayer(args[1]);
								if (player != null) {
									if (args[2] != null) {
										int count = Integer.parseInt(args[2]);
										String name = player.getName();
										setScore(name, count);
										sender.sendMessage("§f>>>§b[INFO]§a" + name + "§fのカウントを§b"+ count +"§fにセットしました");
									}
								}else {
									sender.sendMessage("§f>>>§b[INFO]§fそのPLAYERはサーバーにいません");
								}
							}
						}
						catch(ArrayIndexOutOfBoundsException e){
							sender.sendMessage("§f>>>/oe setcount [PlayerName] [count]");
					}
						break;
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				sender.sendMessage("§f>>>/oc start");	
				sender.sendMessage("§f>>>/oc stop");	
				sender.sendMessage("§f>>>/oe timeset [time]");
				sender.sendMessage("§f>>>/oe reset");
				sender.sendMessage("§f>>>/oe setcount [PlayerName] [count]");
		}		
		}
		return false;
	}
	
    public void setScore(String name, int point) {
    	objective.getScore(name).setScore(point);
    }
    public void removeScore(String name) {
    	objective.getScore(name).setScore(0);
    	scoreboard.resetScores(name);
    }
    public int getScore(String name) {
    	return objective.getScore(name).getScore();
    }
    public void addScore(String name, int amount) {
    	 int point = getScore(name);
    	 setScore(name, point + amount);
    }
    public void addSoubi(Player player) {
		player.getInventory().clear();
		player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		player.getInventory().addItem(new ItemStack(Material.DIAMOND_PICKAXE));
		player.getInventory().addItem(new ItemStack(Material.DIAMOND_PICKAXE));
		player.getInventory().addItem(new ItemStack(Material.DIAMOND_AXE));
		player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
		player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
		player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
		player.getInventory().addItem(new ItemStack(Material.TORCH , 64));
		player.getInventory().addItem(new ItemStack(Material.TORCH , 64));
		player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
    }
}
