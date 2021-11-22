/*import java.util.concurrent.Callable;

public class maxDiviseursAvecExecutor {
    private final static int MAX = 25000;
   
    //Une classe pour représenter le résultat d'une sous-tâche.
	private static class Resultat {
        int maxDiviseurParSousTache;    // Nombre maximal de diviseurs.
        int nombreAvecMaxSousTache;     // Quel entier a donné ce nombre maximal.
		
        public Resultat(int maxDiviseursTh, int nombreAvecMaxTh){
            maxDiviseurParSousTache = maxDiviseursTh;
            nombreAvecMaxSousTache = nombreAvecMaxTh;
        }
    }
    
 
	private static class sousTache implements Callable<Resultat> {
        int min, max; // début et fin de la plage des entiers à traiter
        
		//La sous-tâche est exécutée lorsque la méthode call() est appelée
        public Resultat call() {
            //
			//
            int maxDiviseursTh = 0;
            int nombreAvecMaxTh = 0;
            
            return new Resultat(maxDiviseursTh,nombreAvecMaxTh);
        }
    } 

        
    private static void diviseurCompteurAvecExecutor(int nombreDeThread) {
   
        // Créer l'ExecutorService et un ArrayList pour stocker les Futures
        
        long tDebut = ??
        ExecutorService executor = Executors.newFixedThreadPool(nombreDeThread);
        
        ArrayList<Future<Resultat>> resultats = new ArrayList<>();

        // Créer les sous-tâches et ajouter-les à l'executor. Chaque sous-tâche traite une plage de 1000 entiers.
        
        for (int i = 0; i < nombreDeTaches; i++) {
            //
            // Soumettre la sous-tâche à l'excutor--> retourne un objet de type Future
            // Ajouter l'objet de type Future dans ArrayList
			//
        }
		
		// Au fur et à mesure que l'excutor exécute les tâches, les résultats deviennent disponibles dans les Futures qui sont stockés dans l'ArrayList. 
		// Obtenir les résultats et combiner-les pour produire le résultat final
        //
        for (Future<Resultat> res : resultats) {
            //
        }
        
        
		long tempsEcoule = ??

		// Imprimer le résultat final 
        
        // terminir l'executor.        
    }

    public static void main(String[] args) {
      Scanner clavier = new Scanner(System.in);
      // demander à l'utilisateur le nombre de threads dans le ThreadPool (entre 1 et 100)
      diviseurCompteurAvecExecutor(nombreDeThread);
    }
    

   public static int diviseurCompteur(int N) {
      // Calculer le nombre de diviseurs pour un entier donnée N
   }
} 
*/