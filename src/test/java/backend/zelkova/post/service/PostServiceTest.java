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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    Account account;

    Post post;

    List<Comment> comments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        account = accountRepository.save(
                new Account("name", "nickname", "email@email.com"));
        post = postRepository.save(new Post(account, Category.BOARD, Visibility.PUBLIC, "title", "content"));
        comments.add(commentRepository.save(new Comment(post, account, "첫번째 댓글")));
        comments.add(commentRepository.save(new Comment(post, account, "두번째 댓글")));
        comments.add(commentRepository.save(new Comment(post, account, "세번째 댓글")));
    }

    @AfterEach
    void tearDown() {
        hardDeleteSupplier.hardDelete(Comment.class, Post.class, Account.class);
    }

    @Test
    @Transactional
    @DisplayName("글 조회")
    void getPost() throws Exception {

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

    @Test
    @DisplayName("글 삭제 시 하위 댓글들의 fk null 처리")
    void breakRelation() throws Exception {

        // given

        // when
        postService.delete(account.getId(), post.getId());

        // then
        List<Comment> targetComments = comments.stream()
                .map(comment -> commentRepository.findById(comment.getId()))
                .map(Optional::orElseThrow)
                .toList();
        Assertions.assertThat(targetComments).extracting(Comment::getPost)
                .containsExactly(null, null, null);
    }
}
