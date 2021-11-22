// Tester pour un seul producteur et un seul consommateur
public class ProducteurConsommateurTest {
	public static void main(String[] args) {
		Chapeau c = new Chapeau();
		Producteur p1 = new Producteur(c, 1);
		Consommateur c1 = new Consommateur(c, 1);
		p1.start();
		c1.start();
	}
}