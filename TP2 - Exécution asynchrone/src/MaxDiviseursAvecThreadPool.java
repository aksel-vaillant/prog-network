import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MaxDiviseursAvecThreadPool{
    private final static int MAX = 250000;
    private static ConcurrentLinkedQueue<sousTache> tacheQueue;
    private static LinkedBlockingQueue<Resultat> resultatQueue;

    private static class sousTache {
        int min, max; // début et fin de la plage des entiers à traiter
        int maxDiviseursTh = 0;
        int nombreAvecMaxTh = 0;

        public sousTache(int min, int max) {
            this.min=min;
            this.max=max;
        }

        public void calcul() {
            for(int N=min; N<max; N++){
                if(diviseurCompteur(N) > maxDiviseursTh){
                    maxDiviseursTh = diviseurCompteur(N);
                    nombreAvecMaxTh = N;
                }
            }
            resultatQueue.add( new Resultat(maxDiviseursTh, nombreAvecMaxTh) );
        }
    }

    //Une classe pour représenter le résultat d'une sous-tâche.
    private static class Resultat {
        int maxDiviseurParSousTache;    // Nombre maximal de diviseurs.
        int nombreAvecMaxSousTache;     // Quel entier a donné ce nombre maximal.
;
        public Resultat(int maxDiviseursTh, int nombreAvecMaxTh) {
            maxDiviseurParSousTache=maxDiviseursTh;
            nombreAvecMaxSousTache=nombreAvecMaxTh;
        }

        private void combinerResultat(Resultat resultat) {
            // Combiner les résultats de différents threads
            if(this.maxDiviseurParSousTache < resultat.maxDiviseurParSousTache) {
                this.maxDiviseurParSousTache = resultat.maxDiviseurParSousTache;
                this.nombreAvecMaxSousTache = resultat.nombreAvecMaxSousTache;
            }
        }
    }

    private static class diviseurCompteurThread extends Thread {
        public void run() {
            while (true) {
                sousTache sousTache = tacheQueue.poll();
                if (sousTache != null)
                    sousTache.calcul();
                else
                    return;
            }
        }
    }

    private static void diviseurCompteurAvecThreadPool(int nombreDeThread) throws InterruptedException {
        resultatQueue = new LinkedBlockingQueue<Resultat>();
        tacheQueue = new ConcurrentLinkedQueue<sousTache>();

        // Créer une liste de threads de type diviseurCompteurThread et de taille nombreDeThread
        diviseurCompteurThread[] th = new diviseurCompteurThread[nombreDeThread];
        // Créer un certain nombre des sous-tâches, chaque sous-tâches s'occupe d'un certains nombre d'entiers 1000 par exemple.
        int div = MAX / nombreDeThread;
        // ajouter les sous-tâches à tacheQueue --> tacheQueue.add(new sousTache(debut,fin));
        for (int i = 0; i < nombreDeThread; i++) {
            th[i] = new diviseurCompteurThread();
            int min = div * i;
            int max = div * (i+1);
            tacheQueue.add(new sousTache(min, max));
            //System.out.println("Thread "+ i +" travaille de "+ min +" à " + max);
        }

        // Lancer les threads
        System.out.println("\nDémarrage des threads!");
        Instant start = Instant.now();

        // Démmarer les nombreDeThread threads --> Les threads exécuteront les tâches et les résultats seront placés dans la Queue des résultats resultatQueue.
        // Calculer le résultat final --> lire tous les résultats de la Queue des résultats et combinez-les pour donner la réponse finale.
        Resultat resultatFinal = new Resultat(0,0);
        for (int i = 0; i < nombreDeThread; i++) {
            th[i].start();
            Resultat resultatTemp = resultatQueue.take();
            resultatFinal.combinerResultat(resultatTemp);
        }

        Instant end = Instant.now();

        System.out.println("\nParmis les nombres entiers compris entre 1 et " + MAX + ", le nombre maximum de " +
                "diviseurs est " + resultatFinal.maxDiviseurParSousTache + ". Le plus petit nombre avec "+ resultatFinal.maxDiviseurParSousTache +" diviseurs " +
                "est "+ resultatFinal.nombreAvecMaxSousTache +".");

        //Imprimer le résultat
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds (~" + timeElapsed.getSeconds() + " seconds)");
    }



    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);

        // Demander à l'utilisateur un nombre de threads entre 1 et 100
        System.out.print("Entrez un nombre de threads compris entre 1 et 100: ");
        int nombreDeThread = Integer.parseInt(in.nextLine());
        System.out.println("\nDivision du calcul avec " + nombreDeThread + " thread(s).");
        in.close();

        diviseurCompteurAvecThreadPool(nombreDeThread);
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
