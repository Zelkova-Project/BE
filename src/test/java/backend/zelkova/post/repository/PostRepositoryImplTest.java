package backend.zelkova.post.repository;

import backend.zelkova.IntegrationTestSupport;
import backend.zelkova.account.entity.Account;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.post.dto.response.PostPreviewResponse;
import backend.zelkova.post.dto.response.PostResponse;
import backend.zelkova.post.entity.Post;
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

    Account account;

    Post firstPost;
    Post middlePost;
    Post lastPost;

    @BeforeEach
    void setUp() {
        account = accountRepository.save(new Account("loginId", "password", "name", "nickname", "email"));
        firstPost = postRepository.save(new Post(account, "첫번째", "내용"));
        postRepository.save(new Post(account, "두번째", "내용"));
        middlePost = postRepository.save(new Post(account, "세번째", "내용"));
        postRepository.save(new Post(account, "네번째", "내용"));
        lastPost = postRepository.save(new Post(account, "다섯번째", "내용"));
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("이전글, 다음글 포함해서 조회하기")
    void retrieveNotice() throws Exception {

        // given

        // when
        PostResponse postResponse = postRepository.retrieveNoticeResponse(middlePost.getId());

        // then
        Assertions.assertThat(postResponse).extracting(PostResponse::title, PostResponse::content)
                .containsExactly("세번째", "내용");

        Assertions.assertThat(postResponse.prev().title())
                .isEqualTo("두번째");

        Assertions.assertThat(postResponse.next().title())
                .isEqualTo("네번째");
    }

    @Test
    @DisplayName("이전글 없음")
    void retrieveNoticePrevNull() throws Exception {

        // given

        // when
        PostResponse postResponse = postRepository.retrieveNoticeResponse(firstPost.getId());

        // then
        Assertions.assertThat(postResponse).extracting(PostResponse::title, PostResponse::content)
                .containsExactly("첫번째", "내용");

        Assertions.assertThat(postResponse.prev())
                .isNull();

        Assertions.assertThat(postResponse.next().title())
                .isEqualTo("두번째");
    }

    @Test
    @DisplayName("다음글 없음")
    void retrieveNoticeNextNull() throws Exception {

        // given

        // when
        PostResponse postResponse = postRepository.retrieveNoticeResponse(lastPost.getId());

        // then
        Assertions.assertThat(postResponse).extracting(PostResponse::title, PostResponse::content)
                .containsExactly("다섯번째", "내용");

        Assertions.assertThat(postResponse.prev().title())
                .isEqualTo("네번째");

        Assertions.assertThat(postResponse.next())
                .isNull();
    }

    @Test
    @DisplayName("페이지네이션 확인")
    void retrieveAllNoticePagination() throws Exception {

        // given

        // when
        Page<PostPreviewResponse> noticePreviewResponses = postRepository.retrieveAllNoticesResponses(
                Pageable.ofSize(3));

        // then
        Assertions.assertThat(noticePreviewResponses.getTotalElements())
                .isEqualTo(5);

        Assertions.assertThat(noticePreviewResponses.getTotalPages())
                .isEqualTo(2);
    }
}
