import java.time.Duration;
import java.time.Instant;

public class maxDiviseurs {

   public static void main(String[] args) {

       // De 1 à 25000
       int N = 1;
       final int MAX = 25000;

	   // Variable temporaire qui stocke le plus grand nombre possédant le plus de diviseurs
       int nombreAvecMax = 0;
       // Variable temporaire qui stocke le nombre de diviseurs
       int maxDiviseurs = 0;

       Instant start = Instant.now();

       for(N=2; N<MAX; N++){
           int tempMax = 0;
           for (int i = 1; i<=N; i++) {
               if (N%i == 0) {
                   tempMax++;
               }
           }
           if(tempMax > maxDiviseurs){
               maxDiviseurs = tempMax;
               nombreAvecMax = N;
           }
       }

       Instant end = Instant.now();

       System.out.println("Parmi les nombres entiers compris entre 1 et " + MAX + ", le nombre maximum de " +
               "diviseurs est " + maxDiviseurs + ". Le plus petit nombre avec "+ maxDiviseurs +" diviseurs " +
               "est "+ nombreAvecMax +".");

       //Imprimer le résultat
       Duration timeElapsed = Duration.between(start, end);
       System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");

       /**
        * Tracé du temps : cf RésultatCalcul.png
        *
        * Records avec Max = 25000
        * time ~ 722 ms
        *
        * Records avec Max = 50000
        * time ~ 3022 ms
        *
        * Records avec Max = 100000
        * time ~ 10988 ms
        *
        * Records avec Max = 150000
        * time ~ 27142 ms
        *
        * Records avec Max = 200000
        * time ~ 48232 ms
        *
        * Records avec Max = 250000
        * time ~ 75361 ms
        *
       */


   } 

}