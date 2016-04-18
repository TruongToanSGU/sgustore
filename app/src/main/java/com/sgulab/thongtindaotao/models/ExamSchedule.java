package com.sgulab.thongtindaotao.models;

public class ExamSchedule {
  private String subjectId;
  private String subjectName;
  private int numberOfStudents;
  private String date;
  private String time;
  private int duration;
  private String room;

  public String getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }

  public String getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }

  public int getNumberOfStudents() {
    return numberOfStudents;
  }

  public void setNumberOfStudents(int numberOfStudents) {
    this.numberOfStudents = numberOfStudents;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }
}
