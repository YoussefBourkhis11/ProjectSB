package com.example.sge.repository;

import com.example.sge.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List <Note> findByEtudiantId (Long etudiantId ) ;
    List< Note > findByModuleId (Long moduleId );
    @Query(" SELECT AVG (n . valeur ) FROM Note n WHERE n.etudiant.id = :id ")

    Double calculerMoyenne ( @Param( " id ") Long etudiantId );
}
