// Tester pour plusieurs producteurs (3) et plusieurs consommateurs (3)
public class ProducteurConsommateurTest2 {
	public static void main(String[] args) {
		Chapeau c = new Chapeau();
		Producteur p1 = new Producteur(c, 1); // producteur numéro 1
		Producteur p2 = new Producteur(c, 2); // producteur numéro 2
		Producteur p3 = new Producteur(c, 3); // producteur numéro 3
		Consommateur c1 = new Consommateur(c, 1); // consommateur numéro 1
		Consommateur c2 = new Consommateur(c, 2); // consommateur numéro 2
		Consommateur c3 = new Consommateur(c, 3); // consommateur numéro 3
		p1.start();
		p2.start();
		p3.start();
		c1.start();
		c2.start();
		c3.start();
	}
}