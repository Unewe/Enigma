package org.unewe.enigma.game.entity;
//For updating questions
public class QuestionInfo {
    private int id;
    private String question;
    private String answer;

    private String tip_1;
    private String tip_2;
    private String tip_3;

    private String img_1;
    private String img_2;
    private String img_3;

    public QuestionInfo() {

    }

    public QuestionInfo(int id, String question, String answer, String tip_1, String tip_2, String tip_3, String img_1, String img_2, String img_3) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.tip_1 = tip_1;
        this.tip_2 = tip_2;
        this.tip_3 = tip_3;
        this.img_1 = img_1;
        this.img_2 = img_2;
        this.img_3 = img_3;
    }

    public String toString() {
        return id + " : " + question + " : " + answer + " : " + tip_1 + " : " + tip_2 + " : " + tip_3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTip_1() {
        return tip_1;
    }

    public void setTip_1(String tip_1) {
        this.tip_1 = tip_1;
    }

    public String getTip_2() {
        return tip_2;
    }

    public void setTip_2(String tip_2) {
        this.tip_2 = tip_2;
    }

    public String getTip_3() {
        return tip_3;
    }

    public void setTip_3(String tip_3) {
        this.tip_3 = tip_3;
    }

    public String getImg_1() {
        return img_1;
    }

    public void setImg_1(String img_1) {
        this.img_1 = img_1;
    }

    public String getImg_2() {
        return img_2;
    }

    public void setImg_2(String img_2) {
        this.img_2 = img_2;
    }

    public String getImg_3() {
        return img_3;
    }

    public void setImg_3(String img_3) {
        this.img_3 = img_3;
    }
}
