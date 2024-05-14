package backend.zelkova.notice.repository;

import backend.zelkova.IntegrationTestSupport;
import backend.zelkova.account.entity.Account;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.notice.dto.response.NoticePreviewResponse;
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
