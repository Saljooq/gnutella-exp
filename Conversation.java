public class Conversation {

    private command comm;
    private Integer originPort;
    private Integer port;
    private Integer TTL;
    private String address;
    private String originAddress;
    private Long time;

    public static enum command {
        PING,
        PONG,
        SEARCH,
        FOUND
    };

    public Conversation() {
        this.originPort = 0;
        this.port = 0;
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

    @Override
    public String toString() {
        return "command=" + getCommand() +
                ",address=" + getAddress() + ":" + getPort() +
                ",origin=" + getOriginAddress() + ":" + getOriginPort() + "=";
    }

    public static Conversation unmarshall(String s) {

        // Pattern p = Pattern.compile("{*}");
        // Matcher m = p.matcher(s);
        // s = m.
        System.out.println("Converstion begins with : " + s);

        String[] entries = s.split(",");

        for (String entry : entries)
            System.out.println(entry);

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
                    } else
                        System.out.println("unidentified argument found while unmarshalling");
                } catch (NumberFormatException e) {
                    System.out.println("Error : " + e + "\nArg : " + breakEntries[0] + "\nString : " + breakEntries[1]);
                }

            }

        }

        return conv;

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
