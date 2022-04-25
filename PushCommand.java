import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PushCommand implements Runnable{
    

    public String address;
    public int port;
    public String filename;

    public PushCommand(String toAddress, int toPort, String filename){

        this.address = toAddress;
        this.port = toPort;
        this.filename = filename;
    }


    public Node from;

    public void setNode (Node fromNode){
        this.from = fromNode;
    }

    @Override
    public void run() {

        try{

            Conversation conv = new Conversation();
                    // add the query in conv
            conv.setComm(Conversation.command.PUSH);
            conv.setName(filename);
            conv.setAddress(from.address);
            conv.setPort(from.port);

            DatagramSocket ds = new DatagramSocket();

            String test = conv.toString();

            DatagramPacket packet = new DatagramPacket(test.getBytes(),
                            test.getBytes().length,
                            InetAddress.getByName(address),
                            port);

            ds.send(packet);
            ds.close();
                
            if (from.debug) System.out.println("Push sent : " + conv);

        } catch (Exception e) {
            System.out.println("Something went wrong with sending push. Error : " + e);
        }
        
    }

}
