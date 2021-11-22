public class Compte {
  private int solde = 0;

  public void ajouter(int somme) {
    solde += somme;
    System.out.println(" ajoute " + somme);
  }

  public void retirer(int somme) {
    solde -= somme;
    System.out.println(" retire " + somme);
  }

  synchronized public void operationNulle(int somme) {
    solde += somme;
    System.out.println(" ajoute " + somme);
    solde -= somme;
    System.out.println(" retire " + somme);
  }

  synchronized public int getSolde() {
    return solde;
  }
}