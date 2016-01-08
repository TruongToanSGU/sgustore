package com.sgulab.thongtindaotao.objects;

import java.io.Serializable;

public class MarkSubjectDetail extends MarkSubject implements Serializable {
    private String id;
    private int tc;
    private int percentProcess;
    private int percentExam;
    private float markProcess;
    private float markExam;
    private String classification;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTc() {
        return tc;
    }

    public void setTc(int tc) {
        this.tc = tc;
    }

    public int getPercentProcess() {
        return percentProcess;
    }

    public void setPercentProcess(int percentProcess) {
        this.percentProcess = percentProcess;
    }

    public int getPercentExam() {
        return percentExam;
    }

    public void setPercentExam(int percentExam) {
        this.percentExam = percentExam;
    }

    public float getMarkProcess() {
        return markProcess;
    }

    public void setMarkProcess(float markProcess) {
        this.markProcess = markProcess;
    }

    public float getMarkExam() {
        return markExam;
    }

    public void setMarkExam(float markExam) {
        this.markExam = markExam;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
