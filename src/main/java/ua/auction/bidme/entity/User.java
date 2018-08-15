package ua.auction.bidme.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private final int id;
    private final String email;
    private final String password;
    private final List<Message> messages;

    private User(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.messages = builder.messages;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Message> getMessages() {
        return new ArrayList<>(this.messages);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(messages, user.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, messages);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", messages=" + messages +
                '}';
    }

    public static class Builder {
        private int id;
        private final String email;
        private String password;
        private List<Message> messages;

        public Builder(String email) {
            this.email = email;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }

}
