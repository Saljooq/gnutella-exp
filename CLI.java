import java.util.Date;
import java.util.Scanner;

public class CLI implements Runnable {

    private Node node;

    public CLI(Node n) {
        this.node = n;
    }

    @Override
    public void run() {
        while (true) {

            try {
                Scanner scnr = new Scanner(System.in);

                String input = scnr.nextLine();

                if (input.equals(""))
                    continue;

                String inputBreakDown[] = input.split(" ");

                // first argument should be command and the next should be clustered as other
                // inputs

                if (inputBreakDown.length < 2) {
                    System.out.println("insufficient arguments");
                    continue;
                }

                Conversation in = Conversation.unmarshall("command=" + inputBreakDown[0].toUpperCase() + "," + inputBreakDown[1]);

                switch (in.getCommand()) {
                    case PING:
                        Node newNode = new Node();
                        newNode.address = in.getAddress();
                        newNode.port = in.getPort();
                        newNode.status = Conversation.command.PING;
                        if (node.neighbours.contains(newNode)) {
                            System.out.println("Node already added");
                        }
                        // do nothing
                        else {
                            newNode.lastFetched = (new Date()).getTime();
                            node.neighbours.add(newNode);
                            System.out.println("adding new neighbour => " + newNode.address + ":" + newNode.port);
                            (new Thread(new PingPonger(Conversation.command.PING, node, newNode))).start();
                        }
                        break;

                    case PONG:
                        // cannot initiate
                        System.out.println("You can't PONG, only PING allowed in CLI");
                        break;
                    case QUERY:
                        //
                        long currTime = (new Date()).getTime();
                        // the time, address, query name etc should be consumed from conversation by 
                        // non-original nodes
                        if (in.getTTL() == 0) in.setTTL(2);
                        Query newQuery = new Query(in.getName(), currTime, node.address, node.port, in.getTTL());
                        newQuery.setNode(node);

                        node.processedQueries.add(newQuery);

                        (new Thread(newQuery)).start();

                        break;
                    case HIT:
                        // cannot initiate
                        System.out.println("HIT cannot be initiated from a CLI");
                        break;

                    case PUSH:
                        PushCommand push = new PushCommand(in.getAddress(), in.getPort(), in.getName());
                        push.setNode(node);

                        PushDataReceiver dataReceiver = new PushDataReceiver(in.getName(), node);
                        (new Thread(dataReceiver)).start();

                        (new Thread(push)).start();
                        break;
                    default:
                        //
                        System.out.println("No match found in CLI");
                        break;
                }

            } catch (Exception e) {
                System.out.println("Something went wrong with CLI input. Error : " + e);
            }

        }

    }

    // System.out.println();

}
