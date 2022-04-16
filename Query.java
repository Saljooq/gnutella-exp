public class Query {
    
    public String term;
    public Long time;


    public Query(String term, Long time){
        this.term = term;
        this.time = time;
    }

    public boolean equals(Query q){
        return term.equals(q.term) && (time == q.time);
    }

}
