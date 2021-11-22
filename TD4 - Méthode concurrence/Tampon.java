/*  
 * Pour utiliser correctement ce programme, dans le main, il faut :
 * 			- Créer un objet Tampon de taille N
 * 			- Créer des threads I qui vont déposer des messages : dans le cas présent, des chiffres de 0 à 9
 * 			- Créer un thread S qui va imprimer et lire l'intégralité du tampon avant de le réinitialiser
 */

// CAS 1 : Sémaphore 

/*
import java.util.concurrent.Semaphore;

public class Tampon {
	private int nombreMessage;
	private int tampon[];
	
	Semaphore Smutex;
	Semaphore Simp;
	
	private Tampon(int size) {
		tampon = new int[size];
				
		Simp = new Semaphore(0);
		Smutex = new Semaphore(1);
		
		nombreMessage = 0;
	}
	
	public static void main(String[] args) {
		Tampon tampon = new Tampon(10);
		new Pi("1",tampon).start();
		new Pi("2",tampon).start();
		new Pi("3",tampon).start();
		new Ps(tampon).start();
	}
	
	public void deposer(int i) throws InterruptedException{
		Smutex.acquire();
		tampon[nombreMessage]=i;
		System.out.println("Thread n*:" + Thread.currentThread().getName() + " | Message: " + i);
		
		nombreMessage++;
		
		if(nombreMessage == tampon.length) {
			Simp.release();
		} else {
			Smutex.release();
		}
	}
	
	public void imprimer() throws InterruptedException {
		Simp.acquire();
		System.out.println("----------------------IMPRESSION------------------------");
		for(int i = 0; i<tampon.length;i++){
			System.out.println("Message n* " + i + ": " + tampon[i]);
		}
		System.out.println("----------------END-Impression----------------");
		nombreMessage=0;
		Smutex.release();
	}
}
*/

// CAS 2 : Lock & Conditions

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tampon {
	private int nombreMessage;
	private int tampon[];
	
	private Lock lock = new ReentrantLock();
	private Condition print = lock.newCondition();
	private Condition empty = lock.newCondition();
	
	private Tampon(int size) {
		tampon = new int[size];
		nombreMessage = 0;
	}
	
	public static void main(String[] args) {
		Tampon tampon = new Tampon(10);
		new Pi("1",tampon).start();
		new Pi("2",tampon).start();
		new Pi("3",tampon).start();
		new Ps(tampon).start();
	}
	
	public void deposer(int i) throws InterruptedException{
		lock.lock();
		try {
			tampon[nombreMessage] = i;
			System.out.println("Thread n*:" + Thread.currentThread().getName() + " | Message: " + i);
			nombreMessage++;
			
			if(nombreMessage == tampon.length) {
				print.signal();
				empty.await();
			}
		}
		finally {
			lock.unlock();
		}
	}
	
	public void imprimer() throws InterruptedException {
		lock.lock();
		try {
			print.await();
			System.out.println("----------------Impression--------------------");
		
			for(int i =0;i<tampon.length;i++) {
				System.out.println("Message n* " + i + ": " + tampon[i]);
			}
			
			System.out.println("----------------END-Impression----------------");
			nombreMessage=0;
			empty.signalAll();
		}
		finally {
			lock.unlock();
		}
	}
}

