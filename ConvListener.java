import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
                System.out.println("RECEIVING : " + conv);

                switch (conv.getCommand()) {
                    case PING:
                        //
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
                System.out.println("Something went wrong in ConvListener. Error : " + e);
            }
        }

    }

}
