package ua.auction.bidme.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Lot {
    private int id;
    private String title;
    private String description;
    private String image;
    private int startPrice;
    private int currentPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LotStatus status;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LotStatus getStatus() {
        return status;
    }

    public void setStatus(LotStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lot lot = (Lot) o;
        return id == lot.id &&
                startPrice == lot.startPrice &&
                currentPrice == lot.currentPrice &&
                Objects.equals(title, lot.title) &&
                Objects.equals(description, lot.description) &&
                Objects.equals(image, lot.image) &&
                Objects.equals(startTime, lot.startTime) &&
                Objects.equals(endTime, lot.endTime) &&
                status == lot.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, image, startPrice, currentPrice, startTime, endTime, status);
    }

    @Override
    public String toString() {
        return "Lot{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", startPrice=" + startPrice +
                ", currentPrice=" + currentPrice +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                '}';
    }
}
