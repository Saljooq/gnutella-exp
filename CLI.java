import java.util.Scanner;

public class CLI implements Runnable {

    private Node node;
    public CLI(Node n){
        this.node = node;
    }
    @Override
    public void run() {
        while (true)
        {

            try {
                Scanner scnr = new Scanner(System.in);

                String input = scnr.nextLine();

                if (input.equals("")) continue;

                String inputBreakDown[] = input.split(" ");

                // first argument should be command and the next should be clustered as other inputs

                if (inputBreakDown.length < 2){
                    System.out.println("insufficient arguments");
                    continue;
                }

                Conversation in = Conversation.unmarshall("command="+inputBreakDown[0]+","+inputBreakDown[1]);

                switch (in.getCommand()){
                    case PING:
                        //
                    case PONG:
                        // cannot initiate
                    case SEARCH:
                        //
                    case FOUND:
                        // cannot initiate
                    default:
                        //
                }



            } catch (Exception e){
                System.out.println("Something went wrong with CLI input. Error : " + e);
            }

        }
        
    }




    // System.out.println();
    
}
