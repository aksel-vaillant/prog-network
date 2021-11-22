
public class Pi extends Thread{
	
	private Tampon tampon;
	
	public Pi(String nom, Tampon tampon) {
		super(nom);
		this.tampon = tampon;
	}
	
	public void run() {
		while(true) {
			try {
				
				tampon.deposer((int)(Math.random()*(10)));
				Thread.sleep((long)(Math.random()*(5000)));
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
