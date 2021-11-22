package ex4;
public class Consommateur extends Thread {
	
	private Chapeau chapeau;
	private int number;
	
	public Consommateur(Chapeau c, int number) {
		chapeau = c;
		this.number = number; // Le numéro du consommateur
	}

	public void run() {			
		for (int i = 0; i < 10; i++) {		
			try {
				chapeau.get(number);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
}