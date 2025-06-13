package br.com.ifpe.ratemaster.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_response")
public class ResponseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String comment;

    private String createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserModel userModel;

    @OneToOne
    @JoinColumn(name = "review_id", nullable = false)
    @JsonBackReference
    private ReviewModel review;

    public ResponseModel() {}

    public ResponseModel(String name, String comment, String createdAt, ReviewModel review, UserModel userModel) {
        this.name = name;
        this.comment = comment;
        this.createdAt = createdAt;
        this.review = review;
        this.userModel = userModel;
    }

    public Long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ReviewModel getReview() {
        return review;
    }

    public void setReview(ReviewModel review) {
        this.review = review;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}