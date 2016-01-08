package com.sgulab.thongtindaotao.objects;

import java.io.Serializable;

public class MarkSubject implements Serializable {
    private String name;
    private String classification;
    private float finalMark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public float getFinalMark() {
        return finalMark;
    }

    public void setFinalMark(float finalMark) {
        this.finalMark = finalMark;
    }
}
