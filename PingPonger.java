import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PingPonger implements Runnable {

    private Node from;
    private Node to;
    private Conversation.command status;

    public PingPonger(Conversation.command status, Node from, Node to) {
        this.from = from;
        this.to = to;
        this.status = status;
    }

    @Override
    public void run() {
        try {

            DatagramSocket ds = new DatagramSocket();

            InetAddress toAddress = InetAddress.getByName(to.address);

            // String test = "testing udp connection";

            Conversation conv = new Conversation();
            conv.setAddress(from.address);
            conv.setPort(from.port);
            conv.setCommand(this.status.toString());

            String test = conv.toString();

            DatagramPacket packet = new DatagramPacket(test.getBytes(),
                    test.getBytes().length,
                    toAddress,
                    to.port);

            ds.send(packet);
            System.out.println("Should hopefully be sent");

            ds.close();
        } catch (Exception e) {
            System.out.println("Something went wrong. Error : " + e);
        }

    }

}
