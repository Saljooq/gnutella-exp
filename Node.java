import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Scanner;

public class Node {

    public String address;
    public Integer port;
    public Boolean acknowledged;
    public ArrayList<Node> neighbours;
    public Integer interval;
    public Conversation.command status;
    public Long lastFetched;

    public Node() {

    }

    public boolean equals(Node n) {
        return (address.equals(n.address) && port == n.port);
    }

    public static void main(String[] args) throws IOException {

        // Scanner scnr = new Scanner(System.in);
        // System.out.print("Enter name of folder : ");

        // fetchFolderInfo info = new fetchFolderInfo(scnr.nextLine());

        // ArrayList<String> directory = info.getFileNames();

        // for (String fileName : directory){
        // System.out.println(fileName);
        // }

        DatagramSocket ds = new DatagramSocket(8080);

        // InetAddress address = InetAddress.getByName("localhost");

        // String test = "testing udp connection";

        // System.out.print("Enter hello : ");
        // String hello = scnr.nextLine();

        // if (hello.equals("hello")){
        // System.out.println("congrats of typing hello");
        // }

        (new Thread(new Writer())).start();

        byte[] store = new byte[ds.getReceiveBufferSize()];

        DatagramPacket packet = new DatagramPacket(store, store.length);

        ds.receive(packet);

        char[] storeToChar = new char[store.length];

        for (int i = 0; i < storeToChar.length; i++)
            storeToChar[i] = (char) store[i];

        Conversation conv = Conversation.unmarshall(new String(storeToChar));
        System.out.println("RECEIVING : " + conv);

        ds.close();

        // Once we're done
        // scnr.close();

    }
}

class Writer implements Runnable {

    @Override
    public void run() {

        Scanner scnr = new Scanner(System.in);

        System.out.print("Enter hello : ");
        String hello = scnr.nextLine();

        if (hello.equals("hello")) {
            System.out.println("congrats of typing hello");
        }

    }

}