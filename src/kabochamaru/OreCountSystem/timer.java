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
	        	OreCountSystem.objective.setDisplayName("�c�莞�ԁ�a"+count+"��f��");
				if ( count == 0 ) {
					timer.cancel();
					Bukkit.broadcastMessage("��f>>>��b[INFO]��f�Q�[������a�I����f���܂���");
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
