package com.example.sge.dto;

import com.example.sge.model.Etudiant;
import com.example.sge.model.Note;

import java.util.List;

public class BulletinDTO {

    private Etudiant etudiant ;
    private List< Note > notes ;
    private double moyenneGenerale ;
    private String mention ; // calculee selon le tableau
    private boolean admis ; // true si moyenne >= 10
// Constructeurs , Getters & Setters

    public BulletinDTO() {}

    public BulletinDTO(Etudiant etudiant, List<Note> notes,
                       double moyenneGenerale, String mention, boolean admis) {
        this.etudiant = etudiant;
        this.notes = notes;
        this.moyenneGenerale = moyenneGenerale;
        this.mention = mention;
        this.admis = admis;
    }

    public Etudiant getEtudiant()               { return etudiant; }
    public void setEtudiant(Etudiant etudiant)  { this.etudiant = etudiant; }

    public List<Note> getNotes()                { return notes; }
    public void setNotes(List<Note> notes)      { this.notes = notes; }

    public double getMoyenneGenerale()          { return moyenneGenerale; }
    public void setMoyenneGenerale(double m)    { this.moyenneGenerale = m; }

    public String getMention()                  { return mention; }
    public void setMention(String mention)      { this.mention = mention; }

    public boolean isAdmis()                    { return admis; }
    public void setAdmis(boolean admis)         { this.admis = admis; }

    /** Somme totale des coefficients parmi les notes du bulletin */
    public int getTotalCoefficients() {
        if (notes == null) {
            return 0;
        }
        return notes.stream()
                .mapToInt(n -> n.getModule() != null && n.getModule().getCoefficient() != null
                        ? n.getModule().getCoefficient() : 1)
                .sum();
    }

    /** Somme pondérée (note × coefficient) */
    public double getTotalPondere() {
        if (notes == null) {
            return 0.0;
        }
        return notes.stream()
                .mapToDouble(n -> {
                    int coef = (n.getModule() != null && n.getModule().getCoefficient() != null)
                            ? n.getModule().getCoefficient() : 1;
                    return n.getValeur() * coef;
                })
                .sum();
    }
}
