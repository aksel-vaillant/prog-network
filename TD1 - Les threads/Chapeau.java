public class Chapeau {
	private int contents;
	private boolean available = false;
	
	public synchronized int get(int number) {
		while(!available) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Consommateur "+ number +" prend le jeton " + contents);
		available = false;
		notifyAll();
		return contents;
	}
	
	public synchronized void put(int value,int number) {
		while(available) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		contents = value;
		System.out.println("Producteur " + number + " ajoute le jeton numéro " + contents);
		available = true;
		notifyAll();
	}
}