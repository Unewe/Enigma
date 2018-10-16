package org.unewe.enigma.game.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "enigma_questions")
public class EnigmaQuestion {

    @Id
    private Integer id;
    @Column
    private String question;
    @Column
    private String answers;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionTips> tips;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionImgs> imgs;

    public EnigmaQuestion() {
    }

    public EnigmaQuestion(int id, String question, String answers) {
        this.id = id;
        this.question = question;
        this.answers = answers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public List<QuestionTips> getTips() {
        return tips;
    }

    public void setTips(List<QuestionTips> tips) {
        this.tips = tips;
    }

    public List<QuestionImgs> getImgs() {
        return imgs;
    }

    public void setImgs(List<QuestionImgs> imgs) {
        this.imgs = imgs;
    }

    @Override
    public String toString() {
        return "EnigmaQuestion{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answers='" + answers + '\'' +
                '}';
    }
}
