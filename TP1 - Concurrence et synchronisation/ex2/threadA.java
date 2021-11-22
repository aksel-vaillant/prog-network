package ex2;

import java.util.concurrent.Semaphore;

public class threadA extends Thread{
	
	private int n;
	Semaphore semKey;
	Semaphore semPerm;
	
	public threadA(int n, Semaphore semKey, Semaphore semPerm) {
		this.n = n;
		this.semKey = semKey;
		this.semPerm = semPerm;
	}

	public void run() {
		
		for(int i=0; i<n; i++) {
			try {
				// Acquisition de la clé 
				semKey.acquire();
				
				// Tâche à effectuer
				System.out.print("A");
				Thread.sleep(20);

				// Libération de la deuxième clé
				semPerm.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
