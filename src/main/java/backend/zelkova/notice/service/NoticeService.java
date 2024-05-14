package backend.zelkova.notice.service;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.account.operator.AccountReader;
import backend.zelkova.notice.dto.response.NoticePreviewResponse;
import backend.zelkova.notice.dto.response.NoticeResponse;
import backend.zelkova.notice.entity.Notice;
import backend.zelkova.notice.operator.NoticeReader;
import backend.zelkova.notice.operator.NoticeSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final AccountReader accountReader;
    private final NoticeSupplier noticeSupplier;
    private final NoticeReader noticeReader;

    @Transactional
    public Long write(AccountDetail accountDetail, String title, String content) {
        Account account = accountReader.findAccountById(accountDetail.getAccountId());
        Notice notice = noticeSupplier.supply(account, title, content);
        return notice.getId();
    }

    public Page<NoticePreviewResponse> getNoticePreviews(Pageable pageable) {
        return noticeReader.findAll(pageable);
    }
}
