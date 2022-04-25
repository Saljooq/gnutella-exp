import java.util.ArrayList;
import java.util.List;

public class Conversation {

    private command comm;
    private Integer originPort;
    private Integer port;
    private Integer TTL;
    private String address;
    private String originAddress;
    private Long time;
    private String name;

    public command getComm() {
        return this.comm;
    }

    public void setComm(command comm) {
        this.comm = comm;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static enum command {
        PING,
        PONG,
        QUERY,
        HIT,
        PUSH
    };

    public Conversation() {
        this.originPort = 0;
        this.port = 0;
        this.TTL = 0;
        this.time = 0l;
    }

    public command getCommand() {
        return this.comm;
    }

    public void setCommand(command comm) {
        this.comm = comm;
    }

    public void setCommand(String comm) {
        this.comm = command.valueOf(comm);
    }

    public Integer getOriginPort() {
        return this.originPort;
    }

    public void setOriginPort(Integer OriginPort) {
        this.originPort = OriginPort;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setQueryHitResults(List<QueryResult> files){
        String newName = "";

        for (int i = 0; i < files.size(); i++){
            newName += files.get(i).fileName + ":" + files.get(i).size + "?";
        }


        this.name = newName;
    }

    public static class QueryResult{
        public String fileName;
        public Long size;
    }

    public List<QueryResult> getQueryResults(){


        String[] entries = name.split("\\?");

        ArrayList<QueryResult> result = new ArrayList<>();

        for (String entry : entries){
            String[] breakup = entry.split(":");
            if (breakup.length < 1) continue;
            QueryResult q = new QueryResult();
            q.fileName = breakup[0];
            try{  q.size = Long.parseLong(breakup[1]); } catch (Exception e) { System.out.println("Error in query result parsing number. Error : " + e);}
            result.add(q);
        }

        return result;

    }

    @Override
    public String toString() {
        return "command=" + getCommand() +
                ",address=" + getAddress() + ":" + getPort() +
                ",origin=" + getOriginAddress() + ":" + getOriginPort() + 
                ",TTL=" + getTTL() +
                ",name=" + getName() +
                ",time=" + getTime();
    }

    public static Conversation unmarshall(String s) {

        // System.out.println("Converstion begins with : " + s);

        s = s.trim();

        String[] entries = s.split(",");

        // for (String entry : entries)
        //     System.out.println(entry);

        Conversation conv = new Conversation();

        for (int i = 0; i < entries.length; i++) {

            String[] breakEntries = entries[i].split("=");

            if (breakEntries.length > 1 && breakEntries[1] != null) {
                try {
                    if (breakEntries[0].equals("command"))
                        conv.setCommand(breakEntries[1]);
                    else if (breakEntries[0].equals("origin")) {
                        String[] addrAndPort = breakEntries[1].split(":");
                        conv.setOriginAddress(addrAndPort[0]);
                        conv.setOriginPort(Integer.parseInt(addrAndPort[1]));
                    } else if (breakEntries[0].equals("address")) {
                        String[] addrAndPort = breakEntries[1].split(":");
                        conv.setAddress(addrAndPort[0]);
                        conv.setPort(Integer.parseInt(addrAndPort[1]));
                    } else if (breakEntries[0].equals("name")){
                        conv.setName(breakEntries[1]);
                    } else if (breakEntries[0].equals("TTL")){
                        conv.setTTL(Integer.parseInt(breakEntries[1]));
                    } else if (breakEntries[0].equals("time")){
                        conv.setTime(Long.parseLong(breakEntries[1]));
                    } else
                        System.out.println("unidentified argument found while unmarshalling");
                } catch (NumberFormatException e) {
                    System.out.println("Error : " + e + "\nArg : " + breakEntries[0] + "\nString : " + breakEntries[1]);
                }

            }

        }

        return conv;

    }

    public static String customUnmarshaller(String in, String key){
        String[] entries = in.split(",");

        for (String entry : entries){

            String[] entriesInner = entry.split("=");
            if (entriesInner[0].equals(key)){
                return entriesInner[1];
            }
        }

        return null;

    } 


    /**
     * @return Integer return the TTL
     */
    public Integer getTTL() {
        return TTL;
    }

    /**
     * @param TTL the TTL to set
     */
    public void setTTL(Integer TTL) {
        this.TTL = TTL;
    }

    /**
     * @return String return the senderAddress
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param senderAddress the senderAddress to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return String return the originAddress
     */
    public String getOriginAddress() {
        return originAddress;
    }

    /**
     * @param originAddress the originAddress to set
     */
    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    /**
     * @return Long return the time
     */
    public Long getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Long time) {
        this.time = time;
    }

}

