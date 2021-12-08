package multi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class UDPServer extends Thread{
    private DatagramSocket socket;
    private int port;

    public UDPServer(int port) throws SocketException {
        setPort(port);
        socket = new DatagramSocket(port);
    }

    public void setPort(int port){
        this.port = port;
    }

    public void run() {
        List<Client> listClient = new ArrayList<Client>();

        byte[] buf = new byte[8];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        boolean foundClient = false;

        while (true) {
            System.out.println("Attente de reception d'un paquet.");

            // Réception d'un paquet
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Conversion du paquet reçue bytes en long
            long newNum = ByteUtils.bytesToLong(packet.getData());
            System.out.println("Du client " + packet.getPort() + "... " + newNum);

            // Si on reçois un nombre différent de 0
            // Alors on va vérifier l'existence du serveur et mettre à jour la somme
            // Sinon on va créer un client spécifiquement pour ce nouveau paquet d'un client inconnu par le serveur
            if(newNum != 0){
                // Si le serveur ne connait pas de client, on commence par créer un client
                if(listClient.isEmpty()){
                    Client newClient = new Client(packet.getAddress(), packet.getPort(), newNum);
                    listClient.add(newClient);
                }else{
                    // Mise à jour de la somme au client connu
                    for(Client c: listClient){
                        if(packet.getPort() == c.getPort()){
                            c.addSum(newNum);
                            foundClient = true;
                        }
                    }
                    // Si le client n'a pas été trouvé, on va créer un nouveau client
                    if(!foundClient){
                        Client newClient = new Client(packet.getAddress(), packet.getPort(), newNum);
                        listClient.add(newClient);
                        foundClient = false;
                    }
                }

            }else{
                // Si le serveur reçois un 0 d'un client
                // Alors on va renvoyer la somme des calculs au client
                for (Client c: listClient) {
                    if (c.getPort() == packet.getPort()) {
                        // Conversion long to bytes
                        byte[] sum = ByteUtils.longToBytes(c.getSum());
                        System.out.println("Envoie de la somme au client " + c.getPort() + "... " + c.getSum());

                        // Préparation de l'envoie de la somme
                        InetSocketAddress isa = new InetSocketAddress(c.getAddress().getHostAddress(), c.getPort());
                        DatagramPacket newPacket = new DatagramPacket(sum, sum.length, isa);

                        // Envoie de la somme au client
                        try {
                            socket.send(newPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // Pas de suppression du client pour garder les "logs"
                // Ce "même client" peut se reconnecter et continuer les calculs
            }
        }
    }

    public static void main(String[] args) throws SocketException {
        new UDPServer(8796).start();
    }
}