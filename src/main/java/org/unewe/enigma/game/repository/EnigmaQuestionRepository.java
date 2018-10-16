package org.unewe.enigma.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unewe.enigma.game.entity.EnigmaQuestion;
import org.unewe.enigma.game.entity.QuestionTips;

import java.util.Set;

@Repository
public interface EnigmaQuestionRepository extends JpaRepository<EnigmaQuestion, Integer>{

    @Modifying
    @Query("update EnigmaQuestion eq set eq.question = ?1, eq.answers = ?2 where eq.id = ?3")
    int updateQuestionById(String question, String answer, Integer id);

    @Query("select max(id) from EnigmaQuestion")
    int selectMaxId();
}
