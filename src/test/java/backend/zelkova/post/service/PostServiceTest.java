package backend.zelkova.post.service;

import backend.zelkova.IntegrationTestSupport;
import backend.zelkova.account.entity.Account;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.comment.entity.Comment;
import backend.zelkova.comment.model.PostCommentResponse;
import backend.zelkova.comment.repository.CommentRepository;
import backend.zelkova.helper.HardDeleteSupplier;
import backend.zelkova.helper.TransactionWrapper;
import backend.zelkova.post.dto.response.PostInfoResponse;
import backend.zelkova.post.dto.response.PostResponse;
import backend.zelkova.post.entity.Post;
import backend.zelkova.post.model.Category;
import backend.zelkova.post.model.Visibility;
import backend.zelkova.post.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class PostServiceTest extends IntegrationTestSupport {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostService postService;

    @Autowired
    HardDeleteSupplier hardDeleteSupplier;

    @Autowired
    TransactionWrapper transactionWrapper;

    @AfterEach
    void tearDown() {
        hardDeleteSupplier.hardDelete(Comment.class, Post.class, Account.class);
    }

    @Test
    @Transactional
    @DisplayName("글 조회")
    void getPost() throws Exception {

        // given
        Account account = accountRepository.save(
                new Account("name", "nickname", "email@email.com"));
        Post post = postRepository.save(new Post(account, Category.BOARD, Visibility.PUBLIC, "title", "content"));
        commentRepository.save(new Comment(post, account, "첫번째 댓글"));
        commentRepository.save(new Comment(post, account, "두번째 댓글"));
        commentRepository.save(new Comment(post, account, "세번째 댓글"));

        // when
        PostResponse postResponse = postService.getPost(post.getId());

        // then
        Assertions.assertThat(postResponse.postInfoResponse())
                .extracting(PostInfoResponse::accountId, PostInfoResponse::content)
                .containsExactly(account.getId(), "content");

        Assertions.assertThat(postResponse.postCommentResponses())
                .extracting(PostCommentResponse::comment)
                .containsExactly("첫번째 댓글", "두번째 댓글", "세번째 댓글");
    }
}
