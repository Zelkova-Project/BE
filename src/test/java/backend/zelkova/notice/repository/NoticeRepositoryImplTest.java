package backend.zelkova.notice.repository;

import backend.zelkova.IntegrationTestSupport;
import backend.zelkova.account.entity.Account;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.notice.dto.response.NoticePreviewResponse;
import backend.zelkova.notice.dto.response.NoticeResponse;
import backend.zelkova.notice.entity.Notice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class NoticeRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    AccountRepository accountRepository;

    Account account;

    Notice firstNotice;
    Notice middleNotice;
    Notice lastNotice;

    @BeforeEach
    void setUp() {
        account = accountRepository.save(new Account("loginId", "password", "name", "nickname", "email"));
        firstNotice = noticeRepository.save(new Notice(account, "첫번째", "내용"));
        noticeRepository.save(new Notice(account, "두번째", "내용"));
        middleNotice = noticeRepository.save(new Notice(account, "세번째", "내용"));
        noticeRepository.save(new Notice(account, "네번째", "내용"));
        lastNotice = noticeRepository.save(new Notice(account, "다섯번째", "내용"));
    }

    @AfterEach
    void tearDown() {
        noticeRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("이전글, 다음글 포함해서 조회하기")
    void retrieveNotice() throws Exception {

        // given

        // when
        NoticeResponse noticeResponse = noticeRepository.retrieveNoticeResponse(middleNotice.getId());

        // then
        Assertions.assertThat(noticeResponse).extracting(NoticeResponse::title, NoticeResponse::content)
                .containsExactly("세번째", "내용");

        Assertions.assertThat(noticeResponse.prev().title())
                .isEqualTo("두번째");

        Assertions.assertThat(noticeResponse.next().title())
                .isEqualTo("네번째");
    }

    @Test
    @DisplayName("이전글 없음")
    void retrieveNoticePrevNull() throws Exception {

        // given

        // when
        NoticeResponse noticeResponse = noticeRepository.retrieveNoticeResponse(firstNotice.getId());

        // then
        Assertions.assertThat(noticeResponse).extracting(NoticeResponse::title, NoticeResponse::content)
                .containsExactly("첫번째", "내용");

        Assertions.assertThat(noticeResponse.prev())
                .isNull();

        Assertions.assertThat(noticeResponse.next().title())
                .isEqualTo("두번째");
    }

    @Test
    @DisplayName("다음글 없음")
    void retrieveNoticeNextNull() throws Exception {

        // given

        // when
        NoticeResponse noticeResponse = noticeRepository.retrieveNoticeResponse(lastNotice.getId());

        // then
        Assertions.assertThat(noticeResponse).extracting(NoticeResponse::title, NoticeResponse::content)
                .containsExactly("다섯번째", "내용");

        Assertions.assertThat(noticeResponse.prev().title())
                .isEqualTo("네번째");

        Assertions.assertThat(noticeResponse.next())
                .isNull();
    }

    @Test
    @DisplayName("페이지네이션 확인")
    void retrieveAllNoticePagination() throws Exception {

        // given

        // when
        Page<NoticePreviewResponse> noticePreviewResponses = noticeRepository.retrieveAllNoticesResponses(
                Pageable.ofSize(3));

        // then
        Assertions.assertThat(noticePreviewResponses.getTotalElements())
                .isEqualTo(5);

        Assertions.assertThat(noticePreviewResponses.getTotalPages())
                .isEqualTo(2);
    }
}
