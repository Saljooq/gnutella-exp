import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Query implements Runnable{
    
    public String term;
    public long time;
    public String address;
    public int port;


    public Query(String term, Long time, String address, int port){

        this.term = term;
        this.time = time;
        this.address = address;
        this.port = port;
    }
    public Query(Conversation conv){

        this.term = conv.getName();
        this.time = conv.getTime();
        this.address = conv.getAddress();
        this.port = conv.getPort();
    }


    public Node from;

    public void setNode (Node fromNode){
        this.from = fromNode;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof Query)) {
            return false;
        }
        Query q = (Query) o;

        return term.equals(q.term) && address.equals(q.address) && port == q.port; //&& (time == q.time);
    }

    @Override
    public void run() {

        for(Node to : from.neighbours)
        {

            try {

                DatagramSocket ds = new DatagramSocket();

                InetAddress toAddress = InetAddress.getByName(to.address);

                Conversation conv = new Conversation();
                // add the query in conv
                conv.setComm(Conversation.command.QUERY);
                conv.setName(term);
                conv.setTime(time);
                conv.setAddress(address);
                conv.setPort(port);

                String test = conv.toString();

                DatagramPacket packet = new DatagramPacket(test.getBytes(),
                        test.getBytes().length,
                        toAddress,
                        to.port);

                ds.send(packet);
                if (from.debug) System.out.println("Query sent : " + conv);

                ds.close();
            } catch (Exception e) {
                System.out.println("Something went wrong with sending query. Error : " + e);
            }
        }


        
    }

}
