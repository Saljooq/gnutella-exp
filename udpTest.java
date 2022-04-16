import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class udpTest {

    public static void main(String[] args) throws IOException {
        
        int port = 8080;

        DatagramSocket ds = new DatagramSocket();

        InetAddress address = InetAddress.getByName("localhost");  
        
        // String test = "testing udp connection";

        Conversation conv = new Conversation();
        conv.setSenderPort(8081);
        conv.setCommand("ping");

        String test = conv.toString();


        DatagramPacket packet = new DatagramPacket(test.getBytes(), 
        test.getBytes().length, 
        address,
        port);

        ds.send(packet);
        System.out.println("Should hopefully be sent");

        ds.close();

    }
    
}
