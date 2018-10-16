package org.unewe.enigma.game.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.unewe.enigma.game.entity.EnigmaQuestion;
import org.unewe.enigma.game.entity.QuestionTips;


public interface QuestionTipsRepository extends JpaRepository<QuestionTips, Integer> {
    void deleteByQuestion(EnigmaQuestion question);
    boolean existsByQuestion(EnigmaQuestion question);
}
