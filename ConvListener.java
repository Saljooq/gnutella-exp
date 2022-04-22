import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;
import java.util.Optional;

public class ConvListener implements Runnable {

    private Node node;

    public ConvListener(Node node) {
        this.node = node;
    }

    @Override
    public void run() {

        DatagramSocket ds = null;

        try {
            ds = new DatagramSocket(node.port);
        } catch (Exception e) {
            System.out.println("failed to secure port " + node.port + " Error : " + e);
        }
        while (true) {

            try {

                // Get some string

                byte[] store = new byte[ds.getReceiveBufferSize()];

                DatagramPacket packet = new DatagramPacket(store, store.length);

                ds.receive(packet);

                char[] storeToChar = new char[store.length];

                for (int i = 0; i < storeToChar.length; i++)
                    storeToChar[i] = (char) store[i];

                Conversation conv = Conversation.unmarshall(new String(storeToChar));
                if (node.debug) System.out.println("RECEIVING : " + conv);
                Node newNode;
                Optional<Node> matched;

                switch (conv.getCommand()) {

                    // someone just pinged us, we need to pong them back and update their time if already a neighbout or add them
                    case PING:
                        newNode = new Node();
                        node.status = Conversation.command.PONG;
                        newNode.address = conv.getAddress();
                        newNode.port = conv.getPort();
                        matched = node.neighbours.stream().filter(n -> n.equals(newNode)).findAny();
                        if (matched.isPresent()){
                            matched.get().lastFetched = (new Date()).getTime();
                            if (node.debug) System.out.println("updating time for Node => " + matched.get());
                        }
                        else{
                            newNode.lastFetched = (new Date()).getTime();
                            System.out.println("We just got pinged by a new node. Adding neighbour => " + newNode);
                            node.neighbours.add(newNode);
                        }
                        (new Thread(new PingPonger(Conversation.command.PONG, node, newNode))).start();
                        break;

                    // someone we pinged just ponged us, we update their time
                    case PONG: 
                        newNode = new Node();
                        node.status = Conversation.command.PING;
                        newNode.address = conv.getAddress();
                        newNode.port = conv.getPort();
                        matched = node.neighbours.stream().filter(n -> n.equals(newNode)).findAny();
                        if (matched.isPresent()){
                            matched.get().lastFetched = (new Date()).getTime();
                        }
                        else{
                            // newNode.lastFetched = (new Date()).getTime();
                            // node.neighbours.add(newNode);
                            System.out.println("Received Pong. Node should be on the list but it is not found. Conversation : " + conv.toString());
                            if (node.debug){
                                System.out.print("SUMMERY of neighbours--> ");
                                for (Node neighbour : node.neighbours){
                                    System.out.print(neighbour + "--> ");
                                }
                                System.out.println("\nCURRENT PONG NODE--> " + newNode);

                            }
                        }
                        break;
                    case SEARCH:
                        //
                        break;
                    case FOUND: 
                        System.out.println("FOUND cannot be initiated from a CLI");
                        break;
                    default: 
                        System.out.println("No match found in CLI");
                        break;

                }

            } catch (Exception e) {
                System.out.println("Something went wrong in ConvListener. Error : " + e);
            }
        }

    }

}
