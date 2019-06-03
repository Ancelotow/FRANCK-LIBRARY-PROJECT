package fr.ancelotow.catfacar.entities;

import java.time.LocalDate;

public class Livre {

    private int numRes;
    private String nom;
    private String auteur1;
    private String auteur2;
    private String edition;
    private LocalDate commande;

    public Livre(){
        super();
    }

    public Livre(int numRes, String nom, String auteur1, String auteur2, String edition,
                 LocalDate commande) {
        super();
        this.numRes = numRes;
        this.nom = nom;
        this.auteur1 = auteur1;
        this.auteur2 = auteur2;
        this.edition = edition;
        this.commande = commande;
    }

    public int getNumRes() {
        return numRes;
    }

    public void setNumRes(int numRes) {
        this.numRes = numRes;
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

    public LocalDate getCommande() {
        return commande;
    }

    public void setCommande(LocalDate commande) {
        this.commande = commande;
    }

    @Override
    public String toString(){
        return String.valueOf(numRes);
    }

}
