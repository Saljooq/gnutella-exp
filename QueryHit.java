import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class QueryHit implements Runnable{
    

    public String address;
    public int port;
    public List<Conversation.QueryResult> results;

    public QueryHit(String toAddress, int toPort, List<Conversation.QueryResult> results){

        this.address = toAddress;
        this.port = toPort;
        this.results = results;
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
            conv.setComm(Conversation.command.HIT);
            conv.setQueryHitResults(results);
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
                
            if (from.debug) System.out.println("QueryHit sent : " + conv);

        } catch (Exception e) {
            System.out.println("Something went wrong with sending query. Error : " + e);
        }
        
    }

}
