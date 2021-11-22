package ex2;

import java.util.concurrent.Semaphore;

public class threadC extends Thread{
	
	private int n;
	Semaphore semKey;
	Semaphore semPerm;
	
	public threadC(int n, Semaphore semKey, Semaphore semPerm) {
		this.n = n;
		this.semKey = semKey;
		this.semPerm = semPerm;
	}

	public void run() {
		
		for(int i=0; i<n; i++) {
			try {
				semKey.acquire();
				
				System.out.print("C ");
				Thread.sleep(20);
				
				semPerm.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
