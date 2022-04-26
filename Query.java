import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Query implements Runnable{
    
    public String term;
    public long time;
    public String address;
    public int port;
    public int TTL;


    public Query(String term, Long time, String address, int port, int TTL){

        this.term = term;
        this.time = time;
        this.address = address;
        this.port = port;
        this.TTL = TTL;
    }
    public Query(Conversation conv){

        this.term = conv.getName();
        this.time = conv.getTime();
        this.address = conv.getAddress();
        this.port = conv.getPort();
        this.TTL = conv.getTTL();
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

        // intead of a unique id, we till use time, address of requester and term to determine identity 
        // of port
        return term.equals(q.term) && address.equals(q.address) && port == q.port && (time == q.time);
    }

    @Override
    public void run() {
        if (TTL > 0){

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
                    conv.setTTL(TTL);

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

}
