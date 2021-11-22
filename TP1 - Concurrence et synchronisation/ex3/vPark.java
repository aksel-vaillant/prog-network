package ex3;
import java.util.concurrent.Semaphore;

public class vPark {
	 Semaphore semKey;
 
	 public vPark(Semaphore semKey) {
		 this.semKey = semKey;
	 }
 
	 public static void main(String args[]) throws InterruptedException {
	  // Création d'un parking avec n places
	  int nbCapacite = 4;
		 
	  // Le sémaphore arriver possède 4 clés qui représentent 4 places de parkings
	  Semaphore arriver = new Semaphore(nbCapacite);
	  System.out.println("Le parking possède " + arriver.availablePermits() + " places");
	  
	  // Création du parking avec l'ensemble des clés
	  vPark parking = new vPark(arriver); 
	  
	  for(int i=0; i < 100; i++) { 	  
		   //System.out.println("\n Voiture id: " + i);

		   new Voiture(parking).start(); 
		   
		   int time = (int) (Math.random() * 5);
		   Thread.sleep(time*1000);
	  }
	 } 
  
	/* 
	*  Dans le cas présent, la synchronization des méthodes arriver et partir est une erreur.
	*  Cela va bloquer l'exécution multiple des processus alors que c'est le but même des sémaphores.
	*  On utilise alors un seul sémaphore au nombre de 4 permis/clés permettant de partager l'ensemble des places sur le parking.
	*/
	public void arriver() throws InterruptedException{
		semKey.acquire();
		System.out.println("\n -->> Une voiture vient d'arriver. Le parking poss�de " + semKey.availablePermits() + " place(s) restante(s).");
	 }
	  
	public void partir() {
		semKey.release();
		System.out.println("\n <<-- Une voiture vient de partir. Le parking poss�de " + semKey.availablePermits() + " place(s) restante(s).");	
	}
}