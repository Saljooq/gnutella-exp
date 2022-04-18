import java.util.Date;
import java.util.Iterator;

public class ConvManager implements Runnable {

    private Node node;

    public ConvManager(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        while (true) {

            long currentTime = (new Date()).getTime();
            Iterator<Node> nodesItr = node.neighbours.iterator();

            while (nodesItr.hasNext()) {
                Node n = nodesItr.next();

                if (currentTime > (n.lastFetched + (n.interval * 2))) {
                    System.out.println("Killing node at address=" + n.address + ":" + n.port);
                    nodesItr.remove();
                } else if (n.status == Conversation.command.PING) {
                    (new Thread(new PingPonger(n.status, node, n))).start();

                } else {
                    // do nothing, this is only meant to pro-actively ping nodes and detect dead
                    // ones
                }
            }
            // we wait for a particular interval to conserve system resources
            try {
                Thread.sleep(node.interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
