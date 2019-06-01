package fr.ancelotow.catfacar.entities;

public class Livre {

    private long id;
    private String nom;
    private String auteur1;
    private String auteur2;
    private String edition;

    public Livre(){
        super();
    }

    public Livre(long id, String nom, String auteur1, String auteur2, String edition) {
        super();
        this.id = id;
        this.nom = nom;
        this.auteur1 = auteur1;
        this.auteur2 = auteur2;
        this.edition = edition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAuteur1() {
        return auteur1;
    }

    public void setAuteur1(String auteur1) {
        this.auteur1 = auteur1;
    }

    public String getAuteur2() {
        return auteur2;
    }

    public void setAuteur2(String auteur2) {
        this.auteur2 = auteur2;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

}
