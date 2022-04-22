public class Query {
    
    public String term;
    public Long time;
    public String address;
    public Integer port;


    public Query(String term, Long time, String address, Integer port){

        this.term = term;
        this.time = time;
        this.address = address;
        this.port = port;
    }

    public boolean equals(Query q){
        return term.equals(q.term) && (time == q.time) && address.equals(q.address) && port == q.port;
    }

}
