package com.starsoft.traveldiary.models;

/**
 * Created by Aashish on 10/13/2016.
 */

public class Post {
    private String postId;
    private String postTitle;
    private String postDescription;
    private String postTag;
    private String postUserId;

    public Post(String postId, String postTitle, String postDescription, String postTag, String postUserId) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.postTag = postTag;
        this.postUserId = postUserId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostTag() {
        return postTag;
    }

    public void setPostTag(String postTag) {
        this.postTag = postTag;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }
}
