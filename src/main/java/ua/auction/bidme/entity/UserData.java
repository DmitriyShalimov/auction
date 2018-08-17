package ua.auction.bidme.entity;

import java.util.List;

public class UserData {
    private User user;
    private List<Message> messages;

    public UserData(User user, List<Message> messages) {
        this.user = user;
        this.messages = messages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "user=" + user +
                ", messages=" + messages +
                '}';
    }
}
