package ua.auction.bidme.entity;

import java.time.LocalDateTime;

public class Message {
    private final int id;
    private final String text;
    private final SuccessIndicator indicator;
    private final Lot lot;
    private final User user;
    private final LocalDateTime dateTime;

    private Message(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.indicator = builder.indicator;
        this.lot = builder.lot;
        this.user = builder.user;
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

    public Lot getLot() {
        return lot;
    }

    public User getUser() {
        return user;
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
                ", lot=" + lot +
                ", user=" + user +
                ", dateTime=" + dateTime +
                '}';
    }

    public static class Builder {
        private int id;
        private String text;
        private SuccessIndicator indicator;
        private Lot lot;
        private User user;
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

        public Builder lot(Lot lot) {
            this.lot = lot;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
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
