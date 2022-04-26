import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class PushDataSender implements Runnable{

    String address;
    int port;
    String filename;
    Node fromNode;

    public PushDataSender(String address,
    int port,
    String filename,
    Node fromNode)
    {
        this.address = address;
        this.port = port;
        this.filename = filename;
        this.fromNode = fromNode;
    }

    @Override
    public void run() {
        
        try {

            Socket clientSocket = new Socket(address, port); 

            // create text reader and writer
            DataOutputStream outStream;

            outStream = new DataOutputStream(clientSocket.getOutputStream());

            List<File> files = (new fetchFolderInfo(fromNode.name)).getFiles();

            File match = null;

            for (File f : files){
                if (filename.equals(f.getName())){
                    match = f;
                }
            }

            if (match != null){

                byte[] buf = new byte[(int)match.length()];

                FileInputStream freader = new FileInputStream(match);

                freader.read(buf);
                freader.close();

                outStream.write(buf, 0, buf.length);
                outStream.flush();

                clientSocket.close();

                System.out.println("Successfully sent " + filename + " to " + address + ":" + port);
            }
            else{
                System.out.println("Filename incorrect. Do not have " + filename);
            }
    } catch (IOException e) {
        System.out.println("Something went wrong in push data sender");;
    }

    }

}
