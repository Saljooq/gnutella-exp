import java.io.IOException;
import java.util.ArrayList;

public class Node {

    public String address;
    public Integer port;
    public Boolean acknowledged;
    public ArrayList<Node> neighbours;
    public Integer interval;
    public Conversation.command status;
    public Long lastFetched;
    public Boolean debug;
    public String name;
    public ArrayList<Query> processedQueries;

    public String toString(){
        return "address=" + address + ":" + port + ",lastFetched=" + lastFetched;
    }

    public Node() {
        this.interval = 2000; // 2000 ms interval by default
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        Node n = (Node) o;

        return (address.equalsIgnoreCase(n.address) && port.equals(n.port));
    }

    public static void main(String[] args) throws IOException {

        Node node = new Node();

        // next we initialise all the key variables;
        if (!node.processArguments(args)){
            return;
        }        

        (new Thread(new ConvListener(node))).start();

        (new Thread(new ConvManager(node))).start();

        (new Thread(new CLI(node))).start();
    }

    public Boolean processArguments (String[] args){

        if (args.length < 1){
            System.out.println("Need arguments for address, port, name like this ->  address=localhost,port=8080,name=root");
            return false;
        }
        else{
            Conversation newConv = Conversation.unmarshall(args[0]);
            this.address = newConv.getAddress();
            this.port = newConv.getPort();
            this.name = newConv.getName();
            this.neighbours = new ArrayList<>();
            this.processedQueries = new ArrayList<>();
            this.interval = 2000; // 2000 ms
            String  debugBool = Conversation.customUnmarshaller(args[0], "debug");
            this.debug = debugBool == null ? false : debugBool.equals("true");


            return true;
        }
    }

}
