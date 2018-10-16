package org.unewe.enigma.game.entity;

import javax.persistence.*;

@Entity
public class QuestionTips {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private EnigmaQuestion question;

    @Column
    private String tip;

    public QuestionTips() {

    }

    public QuestionTips(EnigmaQuestion question, String tip) {
        this.question = question;
        this.tip = tip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EnigmaQuestion getQuestion() {
        return question;
    }

    public void setQuestionId(EnigmaQuestion questionId) {
        this.question = questionId;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
