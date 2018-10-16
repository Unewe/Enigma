package org.unewe.enigma.game.entity;

import javax.persistence.*;

@Entity
public class QuestionImgs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private EnigmaQuestion question;
    @Column
    private String url;

    public QuestionImgs() {}
    public QuestionImgs(EnigmaQuestion question, String url) {
        this.question = question;
        this.url = url;
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

    public void setQuestion(EnigmaQuestion question) {
        this.question = question;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
