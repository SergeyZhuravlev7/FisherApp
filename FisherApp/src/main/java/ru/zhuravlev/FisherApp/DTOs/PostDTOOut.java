package ru.zhuravlev.FisherApp.DTOs;

import java.math.BigDecimal;

public class PostDTOOut {

    private int id;

    private String fish;

    private BigDecimal fishWeight;

    private String message;

    private long likesCount;

    private boolean isLikedByThisUser;

    public PostDTOOut() {
    }

    public String getFish() {
        return fish;
    }

    public void setFish(String fish) {
        this.fish = fish;
    }

    public BigDecimal getFishWeight() {
        return fishWeight;
    }

    public void setFishWeight(BigDecimal fishWeight) {
        this.fishWeight = fishWeight;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isLikedByThisUser() {
        return isLikedByThisUser;
    }

    public void setLikedByThisUser(boolean likedByThisUser) {
        isLikedByThisUser = likedByThisUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
