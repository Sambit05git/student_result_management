package com.srms.models;

import jakarta.persistence.*;


@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String topic;
    private String date;
    private String content;
    private String from;
    private String noticeFor;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    public String getNoticeFor() { return noticeFor; }
    public void setNoticeFor(String noticeFor) { this.noticeFor = noticeFor; }
}
