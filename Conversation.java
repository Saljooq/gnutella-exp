public class Conversation {

    private command comm;
    private Integer originPort;
    private Integer senderPort;
    private Integer TTL;
    private String senderAddress;
    private String originAddress;
    private Long time;

    public static enum command {
        PING,
        PONG,
        SEARCH,
        FOUND
    };


    public Conversation(){
        this.originPort = 0;
        this.senderPort = 0;
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

    public Integer getSenderPort() {
        return this.senderPort;
    }

    public void setSenderPort(Integer senderPort) {
        this.senderPort = senderPort;
    }

    @Override
    public String toString() {
        return 
            "command=" + getCommand() +
            ",senderPort=" + getSenderPort() +
            ",originPort=" + getOriginPort() + "="
            ;
    }

    public static Conversation unmarshall(String s){

        // Pattern p = Pattern.compile("{*}");
        // Matcher m = p.matcher(s);
        // s = m.
        System.out.println("Converstion begins with : " + s);

        String[] entries = s.split(",");

        for (String entry: entries)
            System.out.println(entry);

        Conversation conv = new Conversation();


		for (int i = 0; i < entries.length; i++){

            String[] breakEntries = entries[i].split("=");

            if (breakEntries.length > 1 && breakEntries[1] != null){
                try{
                if (breakEntries[0].equals("command"))
				    conv.setCommand(breakEntries[1]);
                else if (breakEntries[0].equals("originPort"))
				    conv.setOriginPort(Integer.parseInt(breakEntries[1]));
                else if (breakEntries[0].equals("senderPort"))
				    conv.setSenderPort(Integer.parseInt(breakEntries[1]));
                else System.out.println("unidentified argument found while unmarshalling");
                } catch (NumberFormatException e){
                    System.out.println("Error : " + e +"\nArg : "+ breakEntries[0] +"\nString : " + breakEntries[1]);
                }

            }

        }

        return conv;

    }

}
