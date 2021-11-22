package ex4;
public class Producteur extends Thread {
	private Chapeau chapeau;
	private int number;
	
	public Producteur(Chapeau c, int number) {
		chapeau = c;
		this.number = number; // Le numéro du producteur
	}
	public void run() {
		for (int i = 0; i < 10; i++) {
			try {
				chapeau.put(i, number);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}