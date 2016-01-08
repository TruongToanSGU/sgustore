package com.sgulab.thongtindaotao.objects;

import java.io.Serializable;
import java.util.List;

public class MarkTerm implements Serializable {
    private int termId;
    private String termShortName;
    private String termFullName;
    private List<MarkSubjectDetail> marks;
    private float termAvg10;
    private float termAvg4;
    private float allTermAvg10;
    private float allTermAvg4;
    private int passedTc;
    private int allPassedTc;
    private int avgConduct;
    private String conductType;

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTermShortName() {
        return termShortName;
    }

    public void setTermShortName(String termShortName) {
        this.termShortName = termShortName;
    }

    public String getTermFullName() {
        return termFullName;
    }

    public void setTermFullName(String termFullName) {
        this.termFullName = termFullName;
    }

    public List<MarkSubjectDetail> getMarks() {
        return marks;
    }

    public void setMarks(List<MarkSubjectDetail> marks) {
        this.marks = marks;
    }

    public float getTermAvg10() {
        return termAvg10;
    }

    public void setTermAvg10(float termAvg10) {
        this.termAvg10 = termAvg10;
    }

    public float getTermAvg4() {
        return termAvg4;
    }

    public void setTermAvg4(float termAvg4) {
        this.termAvg4 = termAvg4;
    }

    public float getAllTermAvg10() {
        return allTermAvg10;
    }

    public void setAllTermAvg10(float allTermAvg10) {
        this.allTermAvg10 = allTermAvg10;
    }

    public float getAllTermAvg4() {
        return allTermAvg4;
    }

    public void setAllTermAvg4(float allTermAvg4) {
        this.allTermAvg4 = allTermAvg4;
    }

    public int getPassedTc() {
        return passedTc;
    }

    public void setPassedTc(int passedTc) {
        this.passedTc = passedTc;
    }

    public int getAllPassedTc() {
        return allPassedTc;
    }

    public void setAllPassedTc(int allPassedTc) {
        this.allPassedTc = allPassedTc;
    }

    public int getAvgConduct() {
        return avgConduct;
    }

    public void setAvgConduct(int avgConduct) {
        this.avgConduct = avgConduct;
    }

    public String getConductType() {
        return conductType;
    }

    public void setConductType(String conductType) {
        this.conductType = conductType;
    }
}
