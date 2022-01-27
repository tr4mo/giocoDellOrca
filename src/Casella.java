public class Casella {

    private int movimento;
    private int numCasella;
    private Casella nextCasella;
    private Casella prevCasella;
    private boolean[] giocatoriPresenti = new boolean[2];

    public Casella(int numCasella) {
        this.numCasella = numCasella;
        this.movimento = 0;
        nextCasella = null;
        prevCasella = null;
    }

    public Casella(int numCasella, int movimento) {
        this.numCasella = numCasella;
        this.movimento = movimento;
        nextCasella = null;
        prevCasella = null;
    }

    public void entraGiocatore(int i){
        giocatoriPresenti[i-1] = true;
    }

    public void esceGiocatore(int i){
        giocatoriPresenti[i-1] = false;
    }

    public int giocatoriPresenti(){

        //3 == Entrambi i giocatori sono sulla casella
        //2 == Giocatore 2 sulla casella
        //1 == Giocarore 1 sulla casella
        //0 == Nessun giocatore sulla casella

        if(giocatoriPresenti[0] && giocatoriPresenti[1]){
            return 3;
        }else if(giocatoriPresenti[0] && !giocatoriPresenti[1]){
            return 1;
        }else if(!giocatoriPresenti[0] && giocatoriPresenti[1]){
            return 2;
        }
        return 0;
    }

    public Casella getPrevCasella() {
        return prevCasella;
    }

    public void setPrevCasella(Casella prevCasella) {
        this.prevCasella = prevCasella;
    }

    public Casella getNextCasella() {
        return nextCasella;
    }

    public void setNextCasella(Casella nextCasella) {
        this.nextCasella = nextCasella;
    }

    public int getMovimento() {
        return movimento;
    }

    public void setMovimento(int movimento) {
        this.movimento = movimento;
    }

    public int getNumCasella() {
        return numCasella;
    }

    public void setNumCasella(int numCasella) {
        this.numCasella = numCasella;
    }


    public void setNext(Casella nextCasella) {
        this.nextCasella = nextCasella;
    }

    public void setPrev(Casella prevCasella) {
        this.prevCasella = prevCasella;
    }
}
