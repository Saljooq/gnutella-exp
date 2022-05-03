import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;

public class PushDataSender implements Runnable {

    String address;
    int port;
    String filename;
    Node fromNode;

    public PushDataSender(String address,
            int port,
            String filename,
            Node fromNode) {
        this.address = address;
        this.port = port;
        this.filename = filename;
        this.fromNode = fromNode;
    }

    @Override
    public void run() {
        // create text reader and writer
        DataOutputStream outStream = null;
        Socket clientSocket = null;
        FileInputStream freader = null;

        try {

            clientSocket = new Socket(address, port);

            outStream = new DataOutputStream(clientSocket.getOutputStream());

            List<File> files = (new fetchFolderInfo(fromNode.name)).getFiles();

            File match = null;

            for (File f : files) {
                if (filename.equals(f.getName())) {
                    match = f;
                }
            }

            if (match != null) {

                byte[] buf = new byte[(int) match.length()];

                freader = new FileInputStream(match);

                freader.read(buf);
                freader.close();

                ByteBuffer sizeBuff = ByteBuffer.allocate(Long.BYTES);
                sizeBuff.putLong(match.length());
                outStream.write(sizeBuff.array());

                outStream.write(buf, 0, buf.length);

                // outStream.flush(); // this seems to be chocking the buffer for outbound
                // packets

                System.out.println("Successfully sent " + filename + " to " + address + ":" + port);
            } else {
                System.out.println("Filename incorrect. Do not have " + filename);
            }
        } catch (IOException e) {
            System.out.println("Something went wrong in push data sender");
        }

        try {
            clientSocket.close();
            freader.close();
            outStream.close();

        } catch (IOException e) {
            System.out.println("Couldn't close the socket for some reason. Error : " + e);
        }

    }

}
