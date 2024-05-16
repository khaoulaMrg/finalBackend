package com.Aswat.Dtos;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class PostDTO {
    private Long id;
    private String name;
    private String content;

    private String postedBy;
    private Date date;
    private byte[] byteImg;
    private MultipartFile img;
private Long CategoryId;

    private boolean approved;


    private boolean posted;

    // Getters and setters




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getByteImg() {
        return byteImg;
    }

    public void setByteImg(byte[] byteImg) {
        this.byteImg = byteImg;
    }

    public MultipartFile getImg() {
        return img;
    }

    public void setImg(MultipartFile img) {
        this.img = img;
    }

    public Long getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(Long categoryId) {
        CategoryId = categoryId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }
}
