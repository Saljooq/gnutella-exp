import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

                
                // System.out.println(storeToChar);
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
                                System.out.print("SUMMARY of neighbours--> ");
                                for (Node neighbour : node.neighbours){
                                    System.out.print(neighbour + "--> ");
                                }
                                System.out.println("\nCURRENT PONG NODE--> " + newNode);

                            }
                        }
                        break;
                    case QUERY:
                        //
                        System.out.println("Received query request : " + conv);
                        Query incomingQuery = new Query(conv);

                        Optional<Query> matchQuery = node.processedQueries.stream().filter(n -> n.equals(incomingQuery)).findAny();
                        if (matchQuery.isPresent()){
                            System.out.println("Already processed this query");
                        }
                        else {
                            node.processedQueries.add(incomingQuery);
                        
                            fetchFolderInfo folderFile = new fetchFolderInfo(node.name);
                            ArrayList<File> files = folderFile.getFiles();

                            List<Conversation.QueryResult> results = new ArrayList<>();

                            for (File file : files){
                                if (file.getName().toLowerCase().contains(incomingQuery.term.toLowerCase())){
                                    System.out.println(file.getName() + " : " + (file.length()/1000) + " KB");
                                    Conversation.QueryResult res = new Conversation.QueryResult();
                                    res.fileName = file.getName();
                                    res.size = file.length();
                                    results.add(res);
                                }
                            }

                            // send the query to all the neighbours
                            Query newQuery = new Query(conv);
                            newQuery.setNode(node);
                            (new Thread(newQuery)).start();


                            // if we have any matches we let the querying node know
                            if (results.size() > 0)
                            {
                                QueryHit newHit = new QueryHit(conv.getAddress(), conv.getPort(), results);
                                newHit.setNode(node);
                                (new Thread(newHit)).start();
                            }

                            
                        }

                        break;
                    case HIT: 
                        // Processing results from nodes that say they got hit

                        System.out.println("We got a hit from = " + conv.getAddress() + ":" + conv.getPort());
                        System.out.println("START RESULTS --->");

                        List<Conversation.QueryResult> results = conv.getQueryResults();

                        for (Conversation.QueryResult res : results){
                            System.out.println(res.fileName + "    " + (res.size/1000) + " KB");
                        }

                        System.out.println("<--- END RESULTS");
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
