package com.example.sge.controller;

import com.example.sge.dto.BulletinDTO;
import com.example.sge.model.Etudiant;
import com.example.sge.model.Note;
import com.example.sge.service.EtudiantService;
import com.example.sge.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/bulletins")
public class BulletinController {

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private NoteService noteService;

    @GetMapping
    public String liste(Model model) {
        List<Etudiant> etudiants = etudiantService.listerTous();
        List<BulletinDTO> bulletins = new ArrayList<>();

        for (Etudiant etudiant : etudiants) {
            BulletinDTO dto = construireBulletin(etudiant);
            bulletins.add(dto);
        }

        model.addAttribute("bulletins", bulletins);
        return "bulletins/liste";

    }

    @GetMapping("/{etudiantId}")
    public String detail(@PathVariable Long etudiantId, Model model) {
        Etudiant etudiant = etudiantService.trouverParId(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable : " + etudiantId));

        BulletinDTO bulletin = construireBulletin(etudiant);
        model.addAttribute("bulletin", bulletin);
        return "bulletins/detail";

    }

    
    private BulletinDTO construireBulletin(Etudiant etudiant) {
        List<Note> notes = noteService.listerParEtudiant(etudiant.getId());

        // Calcul de la moyenne pondérée
        double totalPondere = 0;
        int totalCoef = 0;
        for (Note n : notes) {
            int coef = (n.getModule() != null && n.getModule().getCoefficient() != null)
                    ? n.getModule().getCoefficient() : 1;
            totalPondere += n.getValeur() * coef;
            totalCoef += coef;
        }
        double moyenne = (totalCoef > 0)
                ? Math.round((totalPondere / totalCoef) * 100.0) / 100.0
                : etudiant.getMoyenne();

        String mention = noteService.calculerMention(moyenne);
        boolean admis = moyenne >= 10;

        return new BulletinDTO(etudiant, notes, moyenne, mention, admis);
    }
}
