package ex1;
public class Voiture extends Thread {
 private vPark parking; 
 
 public Voiture (vPark parking) { 
  this.parking = parking; 
 } 
 
 public void run() 
 { 
	parking.arriver(); 

    try {
		// Temps d'attente aléatoire entre 0 à 20 secs 
		Thread.sleep((int) (Math.random() * 20)*1000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
    
    parking.partir();
}
}