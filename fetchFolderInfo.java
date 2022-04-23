import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class fetchFolderInfo {

    private String folderName;

    public fetchFolderInfo(String folderName){
        this.folderName = folderName;
    }

    public ArrayList<File> getFiles(){
        File directory = new File(folderName);

        if (!directory.exists()) {
            directory.mkdir();
        }

        return new ArrayList<File>(Arrays.asList(directory.listFiles()));
    }
}
