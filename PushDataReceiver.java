import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PushDataReceiver implements Runnable {

    String filename;
    Node fromNode;
    int BUFFER_SIZE = 65536;

    public PushDataReceiver(
            String filename,
            Node fromNode) {
        this.filename = filename;
        this.fromNode = fromNode;
    }

    @Override
    public void run() {

        try {

            ServerSocket servertSocket = new ServerSocket(fromNode.port);

            // create text reader and writer
            DataInputStream inStream;

            Socket clientSocket = servertSocket.accept();

            inStream = new DataInputStream(clientSocket.getInputStream());

            (new fetchFolderInfo(fromNode.name)).getFiles(); // this should create the folder necessary

            File newFile = new File(fromNode.name, filename);

            OutputStream os = new FileOutputStream(newFile);

            byte[] buf = new byte[BUFFER_SIZE];
            int inputSize = 0;
            Long start = (new Date()).getTime();
            try {
                while (true) {
                    inputSize = inStream.read(buf);
                    if (inputSize == -1)
                        break;
                    
                    os.write(buf, 0, inputSize);
                }
            } catch (Exception e) {
            } // should end when file has ended

            Long total = (new Date()).getTime() - start;
            float time = total / 1000;

            os.close();

            // inStream;
            // byte[] buf = new byte[2];

            clientSocket.close();
            servertSocket.close();

            System.out.println("Successfully received " + filename + " in " + time + " seconds");

        } catch (IOException e) {
            System.out.println("Something went wrong in push data sender");
            ;
        }

    }

}
