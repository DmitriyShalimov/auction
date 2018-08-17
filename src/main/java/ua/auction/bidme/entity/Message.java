package ua.auction.bidme.entity;

import java.time.LocalDateTime;

public class Message {
    private final int id;
    private final String text;
    private final SuccessIndicator indicator;
    private final int lotId;
    private final int userId;
    private final LocalDateTime dateTime;

    private Message(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.indicator = builder.indicator;
        this.lotId = builder.lotId;
        this.userId = builder.userId;
        this.dateTime = builder.dateTime;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public SuccessIndicator getIndicator() {
        return indicator;
    }

    public int getLotId() {
        return lotId;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", indicator=" + indicator +
                ", lot=" + lotId +
                ", user=" + userId +
                ", dateTime=" + dateTime +
                '}';
    }

    public static class Builder {
        private int id;
        private String text;
        private SuccessIndicator indicator;
        private int lotId;
        private int userId;
        private LocalDateTime dateTime;

        public Builder(String text) {
            this.text = text;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder indicator(SuccessIndicator indicator) {
            this.indicator = indicator;
            return this;
        }

        public Builder lotId(int lotId) {
            this.lotId = lotId;
            return this;
        }

        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder dateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
