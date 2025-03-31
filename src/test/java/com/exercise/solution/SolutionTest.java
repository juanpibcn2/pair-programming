package com.exercise.solution;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {
    @Test
    void testPostCreationAndRollback() {
        Solution platform = new Solution();
        assertTrue(platform.createPost("1", "Post 1 content", 1L));
        assertFalse(platform.createPost("1", "Duplicate Post 1 content", 2L));  // Test duplicate post id
        assertTrue(platform.editPost("1", "Edited Post 1 content"));
        assertTrue(platform.rollbackPostEdit("1"));
        assertEquals("Post 1 content", platform.getPosts().get("1").getContent());
    }

    @Test
    void testCommentCreationEditAndRollback() {
        Solution platform = new Solution();
        platform.createPost("1", "Post 1 content", 1L);
        assertTrue(platform.commentOnPost("1", "C1", "Comment 1", 1L));
        assertTrue(platform.editComment("1", "C1", "Edited Comment 1"));
        assertTrue(platform.rollbackCommentEdit("1", "C1"));
        assertEquals("Comment 1", platform.getComments().get("C1").getContent());
    }

    @Test
    void testLikePostAndQueryTopLiked() {
        Solution platform = new Solution();
        platform.createPost("1", "Post 1 content", 1L);
        platform.createPost("2", "Post 2 content", 2L);
        platform.likePost("1");
        platform.likePost("2");
        platform.likePost("1");
        List<String> topLiked = platform.topNLikedPosts(2);
        assertEquals(List.of("1", "2"), topLiked);
    }

    @Test
    void testCommentAndQueryTopCommented() {
        Solution platform = new Solution();
        platform.createPost("1", "Post 1 content", 1L);
        platform.createPost("2", "Post 2 content", 2L);
        platform.commentOnPost("1", "C1", "Comment 1", 1L);
        platform.commentOnPost("1", "C2", "Comment 2", 2L);
        List<String> topCommented = platform.topNCommentedPosts(2);
        assertEquals(List.of("1", "2"), topCommented);
    }

    @Test
    void testEditAndRollbackMultipleTimes() {
        Solution platform = new Solution();
        platform.createPost("1", "Post 1 content", 1L);
        platform.commentOnPost("1", "C1", "Comment 1", 1L);
        assertTrue(platform.editPost("1", "Edited Post 1 content"));
        assertTrue(platform.editComment("1", "C1", "Edited Comment 1"));
        assertTrue(platform.rollbackPostEdit("1"));
        assertTrue(platform.rollbackCommentEdit("1", "C1"));
        assertEquals("Post 1 content", platform.getPosts().get("1").getContent());
        assertEquals("Comment 1", platform.getComments().get("C1").getContent());
    }

    @Test
    void testDoubleEditAndDoubleRollbackPost() {
        Solution platform = new Solution();
        assertTrue(platform.createPost("2", "Initial Post Content", 2L));
        assertTrue(platform.editPost("2", "First Edit Post Content"));
        assertTrue(platform.editPost("2", "Second Edit Post Content"));
        assertTrue(platform.rollbackPostEdit("2"));  // Rollback to first edit
        assertEquals("First Edit Post Content", platform.getPosts().get("2").getContent());
        assertTrue(platform.rollbackPostEdit("2"));  // Rollback to initial content
        assertEquals("Initial Post Content", platform.getPosts().get("2").getContent());
    }

    @Test
    void testDoubleEditAndDoubleRollbackComment() {
        Solution platform = new Solution();
        platform.createPost("2", "Post Content", 2L);
        assertTrue(platform.commentOnPost("2", "C2", "Initial Comment Content", 2L));
        assertTrue(platform.editComment("2", "C2", "First Edit Comment Content"));
        assertTrue(platform.editComment("2", "C2", "Second Edit Comment Content"));
        assertTrue(platform.rollbackCommentEdit("2", "C2"));  // Rollback to first edit
        assertEquals("First Edit Comment Content", platform.getComments().get("C2").getContent());
        assertTrue(platform.rollbackCommentEdit("2", "C2"));  // Rollback to initial content
        assertEquals("Initial Comment Content", platform.getComments().get("C2").getContent());
    }
}