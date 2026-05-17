package com.example.sge.service;


import com.example.sge.model.Etudiant;
import com.example.sge.model.Note;
import com.example.sge.repository.EtudiantRepository;
import com.example.sge.repository.NoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    public Note ajouterNote(Note note) {
        if (note.getValeur() < 0 || note.getValeur() > 20) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 20.");
        }
        Note savedNote = noteRepository.save(note);
        mettreAJourMoyenne(note.getEtudiant().getId());
        return savedNote;
    }

    public List<Note> listerParEtudiant(Long etudiantId) {
        return noteRepository.findByEtudiantId(etudiantId);
    }

    public List<Note> listerParModule(Long moduleId) {
        return noteRepository.findByModuleId(moduleId);
    }

    public Double calculerMoyenne(Long etudiantId) {
        Double moyenne = noteRepository.calculerMoyenne(etudiantId);
        return (moyenne != null) ? moyenne : 0.0;
    }

    public void mettreAJourMoyenne(Long etudiantId) {
        Double moyenne = calculerMoyenne(etudiantId);
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        etudiant.setMoyenne(moyenne);
        etudiantRepository.save(etudiant);
    }

    public Note modifier(Long id, Note newNote) {
        Note note = noteRepository.findById(id).orElseThrow();
        note.setValeur(newNote.getValeur());
        Note updated = noteRepository.save(note);
        mettreAJourMoyenne(note.getEtudiant().getId());
        return updated;
    }

    public void supprimer(Long id) {
        Note note = noteRepository.findById(id).orElseThrow();
        Long etudiantId = note.getEtudiant().getId();
        noteRepository.deleteById(id);
        mettreAJourMoyenne(etudiantId);
    }

    public String calculerMention(double moyenne) {
        if (moyenne >= 16)       return "Excellent";
        else if (moyenne >= 14)  return "Très bien";
        else if (moyenne >= 12)  return "Bien";
        else if (moyenne >= 10)  return "Passable";
        else                     return "Ajourné";
    }
}
