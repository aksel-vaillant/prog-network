package ex2;

import java.util.concurrent.Semaphore;

public class testSemaphore{
    
    public static void main(String args[]) throws InterruptedException {
		// Création de 3 sémaphores qui fonctionnent comme des clés de permission

    	Semaphore semA = new Semaphore(1);
    	Semaphore semB = new Semaphore(1);
    	Semaphore semC = new Semaphore(1);
    	
    	// Permet de bloquer les threads B et C au début => exécution du thread A en premier
    	semB.acquire();
    	semC.acquire();

		// Le nombre d'exécution des threads
    	int n = 5; 	

		// On passe en paramètre 2 semaphores qui contrôle l'exécution des threads
    	new threadA(n, semA,semB).start();
    	new threadB(n, semB,semC).start();
    	new threadC(n, semC,semA).start();    	
    }
}
