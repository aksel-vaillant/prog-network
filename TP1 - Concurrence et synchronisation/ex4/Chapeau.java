package ex4;

import java.util.concurrent.Semaphore;


public class Chapeau {
	private int contents;
	Semaphore semKey;
	
    public Chapeau(int n) {
    	this.semKey = new Semaphore(n);
    }
	
	public void get(int number) throws InterruptedException{

		//System.out.println(semKey.availablePermits());

		while(semKey.availablePermits()==0) {
			semKey.release();
		
			System.out.println(" <--- Consommateur "+ number +" prend le jeton " + contents);
			
			Thread.sleep(1000*2);
		}
	}
	
	public void put(int value,int number) throws InterruptedException {
		semKey.acquire();
		
		contents = value;
		
		System.out.println(" ---> Producteur " + number + " ajoute le jeton numéro " + value);
	}
}