package kabochamaru.OreCountSystem;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;



public class timer {
	public static Timer timer;
	public static void startTimer(int time){
		timer = new Timer();
		TimerTask task = new TimerTask() {
			int count = OreCountSystem.time;
			public void run() {
	        	OreCountSystem.objective.setDisplayName("c‚èŠÔ˜a"+count+"˜f•ª");
				if ( count == 0 ) {
					timer.cancel();
					Bukkit.broadcastMessage("˜f>>>˜b[INFO]˜fƒQ[ƒ€‚ª˜aI—¹˜f‚µ‚Ü‚µ‚½");
				}
				count--;
	        }
	    };
	    timer.scheduleAtFixedRate(task,0,time); 
		}
	
	public static void stopTimer() {
		timer.cancel();
	}
}
