package ex1;
public class vPark {
 
 public int capacite;
 
 public vPark(int capacite) {
	 this.capacite = capacite;
 }
 
 public static void main(String args[]) { 
  // Création d'un parking avec n places
  vPark parking = new vPark(4); 
  
  // Création des voitures/threads
  for(int i=0; i < 100; i++) { 
	   new Voiture(parking).start(); 
	   try {
		// Temps d'attente aléatoire entre 0 et 5 secs
		Thread.sleep((int) (Math.random() * 5)*1000);
	   } 
	   catch (InterruptedException e) {
			e.printStackTrace();
	   }
  }
 } 
  
 	/*
 	*  Méthodes synchronized
 	*		- arriver : on bloque les voitures qui ne peuvent pas se garer.
 	*					dans le cas contraire, la voiture prends une place et le signale à l'ensemble des threads/voitures
 	*		
	*		- partir : on signale à l'ensemble des threads/voitures, la disponibilité d'une place de parking
	*/

	public synchronized void arriver(){
		while(capacite==0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notifyAll();
		System.out.println("\n Une voiture vient d'arriver. \n Le parking poss�de " + capacite + " place(s) restante(s).");
		capacite--;
	 }
	  
	public synchronized void partir() {
		notifyAll();
		System.out.println("\n Une voiture vient de partir. \n Le parking poss�de " + capacite + " place(s) restante(s).");
		capacite++;
	}

}