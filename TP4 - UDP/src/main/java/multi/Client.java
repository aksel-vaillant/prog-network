package multi;

import java.net.InetAddress;

public class Client {
    private InetAddress address;
    private int port;
    private long sum;

    public Client(){

    }

    public Client(InetAddress address, int port, long sum) {
        setAddress(address);
        setPort(port);
        setSum(sum);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port){
        this.port = port;
    }

    public long getSum(){
        return this.sum;
    }

    public void setSum(long sum){
        this.sum = sum;
    }

    public void addSum(long sum){
        this.sum += sum;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address){
        this.address = address;
    }
}