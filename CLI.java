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

                Conversation in = Conversation.unmarshall("command=" + inputBreakDown[0] + "," + inputBreakDown[1]);

                switch (in.getCommand()) {
                    case PING:
                        Node newNode = new Node();
                        newNode.address = in.getAddress();
                        newNode.port = in.getPort();
                        if (node.neighbours.contains(newNode)) {
                            System.out.println("Node already added");
                        }
                        // do nothing
                        else {
                            (new Thread(new PingPonger(Conversation.command.PING, node, newNode))).start();
                        }

                    case PONG:
                        // cannot initiate
                        System.out.println("You can't PONG, only PING allowed in CLI");
                    case SEARCH:
                        //
                    case FOUND:
                        // cannot initiate
                        System.out.println("FOUND cannot be initiated from a CLI");
                    default:
                        //
                        System.out.println("No match found in CLI");
                }

            } catch (Exception e) {
                System.out.println("Something went wrong with CLI input. Error : " + e);
            }

        }

    }

    // System.out.println();

}
