
public class Ps extends Thread{
	private Tampon tampon;
	
	public Ps(Tampon tampon) {
		this.tampon = tampon;
	}
	
	public void run() {
		while(true) {
			try {
				tampon.imprimer();
				Thread.sleep((long)(Math.random()*(5000-0)));
			
			} catch (InterruptedException e) {
				e.printStackTrace();
				}
		}
	}
}