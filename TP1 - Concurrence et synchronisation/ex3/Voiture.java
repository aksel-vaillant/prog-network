package ex3;
public class Voiture extends Thread {// � completer 
	private vPark parking; 
	
	public Voiture (vPark parking) { 
	this.parking = parking; 
	} 
	
	public void run() { 
		try {
			parking.arriver();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		try {
			//System.out.println(time + " secs");
			Thread.sleep((int) (Math.random() * 20 + 1)*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
		parking.partir();
	}
}