package backend.zelkova.comment.operator;

import backend.zelkova.IntegrationTestSupport;
import backend.zelkova.account.entity.Account;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.comment.entity.Comment;
import backend.zelkova.comment.repository.CommentRepository;
import backend.zelkova.helper.HardDeleteSupplier;
import backend.zelkova.helper.TransactionWrapper;
import backend.zelkova.post.entity.Post;
import backend.zelkova.post.model.Category;
import backend.zelkova.post.model.Visibility;
import backend.zelkova.post.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentSupplierTest extends IntegrationTestSupport {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentSupplier commentSupplier;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    HardDeleteSupplier hardDeleteSupplier;

    @Autowired
    TransactionWrapper transactionWrapper;

    @AfterEach
    void tearDown() {
        hardDeleteSupplier.hardDelete(Comment.class, Post.class, Account.class);
    }

    @Test
    @DisplayName("댓글 작성")
    void writeComment() throws Exception {

        // given
        Account account = accountRepository.save(
                new Account("name", "nickname", "email@email.com"));
        Post post = postRepository.save(new Post(account, Category.BOARD, Visibility.PUBLIC, "title", "content"));

        // when
        Comment comment = transactionWrapper.commit(() ->
                commentSupplier.supply(post.getId(), account.getId(), "comment"));

        // then
        Assertions.assertThat(comment.getContent())
                .isEqualTo("comment");

        Assertions.assertThat(comment.getAccount().getId())
                .isEqualTo(account.getId());

        Assertions.assertThat(comment.getPost().getId())
                .isEqualTo(post.getId());
    }
}
