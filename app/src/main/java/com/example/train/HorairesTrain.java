package com.example.train;
public class HorairesTrain {
    private String heureDepart;
    private String heureArrivee;

    private String villeDepart;
    private String villeArrivee;

    public HorairesTrain(String heureDepart, String heureArrivee, String villeDepart, String villeArrivee) {
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
    }

    public String getHeureDepart() {
        return heureDepart;
    }

    public String getHeureArrivee() {
        return heureArrivee;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }
}
