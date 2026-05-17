package com.example.sge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue ( strategy = GenerationType . IDENTITY )

    private Long id ;
    private double valeur ; // note sur 20

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;
// Constructeurs , Getters & Setters

    public Note() {}

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public double getValeur()                  { return valeur; }
    public void setValeur(double valeur)       { this.valeur = valeur; }

    public Etudiant getEtudiant()              { return etudiant; }
    public void setEtudiant(Etudiant etudiant) { this.etudiant = etudiant; }

    public Module getModule()                  { return module; }
    public void setModule(Module module)       { this.module = module; }


}
