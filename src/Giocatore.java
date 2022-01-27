class Giocatore {
    private String nome;
    private int punti;
    private int casella;

    public Giocatore(String nome, int punti, int casella) {
        this.nome = nome;
        this.punti = punti;
        this.casella = casella;
    }

    public String getNome() {
        return nome;
    }

    public int getPunti() {
        return punti;
    }

    public void setCasella(int casella) {
        this.casella = casella;
    }

    public int getCasella(){
        return casella;
    }

    public void aggiungiPunti(int punti) {
        this.punti += punti;
    }
}
