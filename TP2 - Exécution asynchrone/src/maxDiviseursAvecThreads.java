import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class maxDiviseursAvecThreads {

   private final static int MAX = 250000;
   private volatile static int maxDiviseurs = 0;
   private volatile static int nombreAvecMax;
   private volatile static int nombreDeThread;

   private static void combinerResultat(int maxDiviseurParThread, int nombreAvecMaxParThread) {
      // Combiner les résultats de différents threads
       if(maxDiviseurs < maxDiviseurParThread){
           maxDiviseurs = maxDiviseurParThread;
           nombreAvecMax = nombreAvecMaxParThread;
       }
   }
   
   private static class diviseurCompteurThread extends Thread {
      int min, max;

      public void run() {
          // Calculer le nombre maximum de diviseurs pour les entiers entre min et max : maxDiviseursTh,nombreAvecMaxTh
          int maxDiviseursTh = 0;
          int nombreAvecMaxTh = 0;

          for(int N=min; N<max; N++){
              if(diviseurCompteur(N) > maxDiviseursTh){
                  maxDiviseursTh = diviseurCompteur(N);
                  nombreAvecMaxTh = N;
              }
          }
          combinerResultat(maxDiviseursTh,nombreAvecMaxTh);
      }
   }

    private static void diviseurCompteurAvecThread(int nombreDeThread) throws InterruptedException {
        // Départager les entiers entre 1 et MAX entre les nombreDeThread Threads
        diviseurCompteurThread[] th = new diviseurCompteurThread[nombreDeThread];
        for (int i = 0; i < nombreDeThread; i++) {
            th[i] = new diviseurCompteurThread();
            if (i == 0) {
                th[i].min = 0;
                th[i].max = MAX / nombreDeThread;
            } else {
                th[i].min = th[i - 1].max;
                th[i].max = MAX / nombreDeThread + th[i].min;
            }
            //System.out.println("Thread " + i + " travaille de " +  + th[i].min + " à " + th[i].max);
        }

        // Lancer les threads
        System.out.println("\nDémarrage des threads!");
        Instant start = Instant.now();

        for (int i = 0; i < nombreDeThread; i++) {
            //System.out.println("Thread " + i + " started!");
            th[i].start();
        }

        // Tester si les threads sont toujours en vies
        for (int i = 0; i < nombreDeThread; i++) {
            if (th[i].isAlive()) {
                th[i].join();
            }
        }

        Instant end = Instant.now();

        System.out.println("\nParmis les nombres entiers compris entre 1 et " + MAX + ", le nombre maximum de " +
                "diviseurs est " + maxDiviseurs + ". Le plus petit nombre avec " + maxDiviseurs + " diviseurs " +
                "est " + nombreAvecMax + ".");

        //Imprimer le résultat
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds (~" + timeElapsed.getSeconds() + " seconds)");
    }
   
   public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);

        // Demander à l'utilisateur un nombre de threads entre 1 et 100
        System.out.print("Entrez un nombre de threads compris entre 1 et 100: ");
        nombreDeThread = Integer.parseInt(in.nextLine());
        System.out.println("\nDivision du calcul avec " + nombreDeThread + " thread(s).");
        in.close();

        diviseurCompteurAvecThread(nombreDeThread);
   }
   
   public static int diviseurCompteur(int N) {
      // Calculer le nombre de diviseurs pour un entier donnée N
       int tempMax = 0;
       for (int i = 1; i<=N; i++) {
           if (N%i == 0) {
               tempMax++;
           }
       }
       return tempMax;
   }
   
}

/**
 * Tracage des données acquis avec N = 250000
 * et n représente le nombre de thread utilisé pour les sous-tâches
 *
 * n = 2
 * time ~ 46866 ms
 *
 * n = 4
 * time ~ 28041 ms
 *
 * n = 8
 * time ~ 15473 ms
 *
 * n = 16
 * time ~ 10164 ms
 *
 * n = 32
 * time ~ 8484 ms
 *
 * n =  64
 * time ~ 7610 ms
 *
 */