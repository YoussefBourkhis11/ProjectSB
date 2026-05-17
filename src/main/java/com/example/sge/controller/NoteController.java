package com.example.sge.controller;

import com.example.sge.model.Note;
import com.example.sge.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity<Note> ajouterNote(@RequestBody Note note) {
        return new ResponseEntity<>(noteService.ajouterNote(note), HttpStatus.CREATED);
    }

    @GetMapping("/etudiant/{id}")
    public List<Note> notesParEtudiant(@PathVariable Long id) {
        return noteService.listerParEtudiant(id);
    }

    @GetMapping("/module/{id}")
    public List<Note> notesParModule(@PathVariable Long id) {
        return noteService.listerParModule(id);
    }

    @GetMapping("/moyenne/{etudiantId}")
    public Double moyenneCalculee(@PathVariable Long etudiantId) {
        return noteService.calculerMoyenne(etudiantId);
    }

    @PutMapping("/{id}")
    public Note modifierNote(@PathVariable Long id, @RequestBody Note note) {
        return noteService.modifier(id, note);
    }

    @DeleteMapping("/{id}")
    public void supprimerNote(@PathVariable Long id) {
        noteService.supprimer(id);
    }
}
