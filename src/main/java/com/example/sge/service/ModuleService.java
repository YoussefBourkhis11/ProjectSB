package com.example.sge.service;

import com.example.sge.model.Etudiant;
import com.example.sge.model.Module;
import com.example.sge.repository.EtudiantRepository;
import com.example.sge.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    public Module ajouter(Module m) {
        return moduleRepository.save(m);
    }

    public List<Module> listerTous() {
        return moduleRepository.findAll();
    }

    public Optional<Module> trouverParId(Long id) {
        return moduleRepository.findById(id);
    }

    public List<Etudiant> trouverEtudiants(Long moduleId) {
        Module m = moduleRepository.findById(moduleId)
                .orElseThrow(() ->
                        new RuntimeException("Module introuvable : "
                                + moduleId));
        return etudiantRepository.findByModule(m);
    }

    public void supprimer(Long id) {
        moduleRepository.deleteById(id);
    }
}