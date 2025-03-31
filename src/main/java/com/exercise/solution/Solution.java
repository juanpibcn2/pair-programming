package com.exercise.solution;

import java.util.*;

public class Solution {
    private Map<String, Post> posts;
    private Map<String, Comment> comments;

    public Solution() {
        this.posts = new HashMap<>();
        this.comments = new HashMap<>();
    }

    public boolean createPost(String postId, String content, long timestamp) {
        if (posts.containsKey(postId)) {
            return false;
        }
        posts.put(postId, new Post(content, timestamp));
        return true;
    }

    public boolean editPost(String postId, String newContent) {
        if (!posts.containsKey(postId)) {
            return false;
        }
        Post post = posts.get(postId);
        post.history.push(new Post(post));
        post.setContent(newContent);
        return true;
    }

    public boolean deletePost(String postId) {
        if (!posts.containsKey(postId)) {
            return false;
        }
        posts.remove(postId);
        return true;
    }

    public boolean likePost(String postId) {
        if (!posts.containsKey(postId)) {
            return false;
        }
        posts.get(postId).incrementLikes();
        return true;
    }

    public List<String> topNLikedPosts(int n) {
        return posts.entrySet().stream()
                .sorted((e1, e2) -> {
                    int cmp = Integer.compare(e2.getValue().getLikes(), e1.getValue().getLikes());
                    return cmp != 0 ? cmp : Long.compare(e1.getValue().getTimestamp(), e2.getValue().getTimestamp());
                })
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    // Getters for testing purposes
    public Map<String, Post> getPosts() {
        return posts;
    }

    public Map<String, Comment> getComments() {
        return comments;
    }

    public boolean commentOnPost(String postId, String commentId, String content, long timestamp) {
        if (!posts.containsKey(postId) || comments.containsKey(commentId)) {
            return false;
        }
        comments.put(commentId, new Comment(content, timestamp));
        posts.get(postId).addComment(commentId);
        return true;
    }

    public List<String> topNCommentedPosts(int n) {
        return posts.entrySet().stream()
                .sorted((e1, e2) -> {
                    int cmp = Integer.compare(e2.getValue().getComments().size(), e1.getValue().getComments().size());
                    return cmp != 0 ? cmp : Long.compare(e1.getValue().getTimestamp(), e2.getValue().getTimestamp());
                })
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    public boolean editComment(String postId, String commentId, String newComment) {
        if (!posts.containsKey(postId) || !comments.containsKey(commentId))
            return false;
        Comment comment = comments.get(commentId);
        comment.history.push(new Comment(comment));

        comment.setContent(newComment);

        return true;
    }

    public boolean rollbackPostEdit(String postId){

        Post post = posts.get(postId);
        if (post.history.isEmpty()) return false;

        posts.replace(postId, post.history.pop());

        return true;
    }

    public boolean rollbackCommentEdit(String postId, String commentId){
        if (!posts.containsKey(postId) || !comments.containsKey(commentId)) return false;
        Comment comment = comments.get(commentId);
        if (comment.history.isEmpty()) return false;
        comments.replace(commentId, comment.history.pop());
        return true;
    }

    public static class Post {
        private String content;
        private long timestamp;
        private int likes;
        private List<String> comments;
        private Stack<Post> history;

        public Post(String content, long timestamp) {
            this.content = content;
            this.timestamp = timestamp;
            this.likes = 0;
            this.comments = new ArrayList<>();
            this.history = new Stack<>();
        }

        public Post(Post post){
            this.content = post.content;
            this.timestamp = post.timestamp;
            this.likes = post.likes;
            this.comments = post.comments;
            this.history = post.history;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public int getLikes() {
            return likes;
        }

        public void incrementLikes() {
            this.likes++;
        }

        public List<String> getComments() {
            return comments;
        }

        public void addComment(String commentId) {
            this.comments.add(commentId);
        }
    }

    public static class Comment {
        private String content;
        private long timestamp;
        private Stack<Comment> history;

        public Comment(String content, long timestamp) {
            this.content = content;
            this.timestamp = timestamp;
            this.history = new Stack<>();
        }

        public Comment(Comment comment){
            this.content = comment.content;
            this.timestamp = comment.timestamp;
            this.history = comment.history;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}