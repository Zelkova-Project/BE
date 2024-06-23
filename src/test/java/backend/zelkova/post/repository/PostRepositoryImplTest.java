package backend.zelkova.post.repository;

import static backend.zelkova.post.model.Category.BOARD;

import backend.zelkova.IntegrationTestSupport;
import backend.zelkova.account.entity.Account;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.helper.HardDeleteSupplier;
import backend.zelkova.post.dto.response.PostInfoResponse;
import backend.zelkova.post.dto.response.PostPreviewResponse;
import backend.zelkova.post.entity.Post;
import backend.zelkova.post.model.Visibility;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class PostRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    PostRepository postRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    HardDeleteSupplier hardDeleteSupplier;

    Account account;

    Post firstPost;
    Post middlePost;
    Post lastPost;

    @AfterEach
    void tearDown() {
        hardDeleteSupplier.hardDelete(Post.class, Account.class);
    }

    @BeforeEach
    void setUp() {
        account = accountRepository.save(new Account("name", "nickname", "email"));

        firstPost = postRepository.save(createPost(account, "첫번째"));
        postRepository.save(createPost(account, "두번째"));
        middlePost = postRepository.save(createPost(account, "세번째"));
        postRepository.save(createPost(account, "네번째"));
        lastPost = postRepository.save(createPost(account, "다섯번째"));
    }

    private Post createPost(Account account, String title) {
        return new Post(account, BOARD, Visibility.PUBLIC, title, "content");
    }

    @Test
    @DisplayName("이전글, 다음글 포함해서 조회하기")
    void retrieveNotice() throws Exception {

        // given

        // when
        PostInfoResponse postInfoResponse = postRepository.retrievePostResponse(middlePost.getId());

        // then
        Assertions.assertThat(postInfoResponse.title())
                .isEqualTo("세번째");

        Assertions.assertThat(postInfoResponse.prev().title())
                .isEqualTo("두번째");

        Assertions.assertThat(postInfoResponse.next().title())
                .isEqualTo("네번째");
    }

    @Test
    @DisplayName("이전글 없음")
    void retrieveNoticePrevNull() throws Exception {

        // given

        // when
        PostInfoResponse postInfoResponse = postRepository.retrievePostResponse(firstPost.getId());

        // then
        Assertions.assertThat(postInfoResponse.title())
                .isEqualTo("첫번째");

        Assertions.assertThat(postInfoResponse.prev())
                .isNull();

        Assertions.assertThat(postInfoResponse.next().title())
                .isEqualTo("두번째");
    }

    @Test
    @DisplayName("다음글 없음")
    void retrieveNoticeNextNull() throws Exception {

        // given

        // when
        PostInfoResponse postInfoResponse = postRepository.retrievePostResponse(lastPost.getId());

        // then
        Assertions.assertThat(postInfoResponse.title())
                .isEqualTo("다섯번째");

        Assertions.assertThat(postInfoResponse.prev().title())
                .isEqualTo("네번째");

        Assertions.assertThat(postInfoResponse.next())
                .isNull();
    }

    @Test
    @DisplayName("페이지네이션 확인")
    void retrieveAllNoticePagination() throws Exception {

        // given

        // when
        Page<PostPreviewResponse> noticePreviewResponses = postRepository.retrieveAllPostPreviewResponses(
                BOARD, Pageable.ofSize(3));

        // then
        Assertions.assertThat(noticePreviewResponses.getTotalElements())
                .isEqualTo(5);

        Assertions.assertThat(noticePreviewResponses.getTotalPages())
                .isEqualTo(2);
    }
}
