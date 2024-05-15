package backend.zelkova.notice.service;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.entity.Role;
import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.account.operator.AccountReader;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
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

    public NoticeResponse getNotice(Long noticeId) {
        return noticeReader.findNoticeResponseByNoticeId(noticeId);
    }

    @Transactional
    public void update(AccountDetail accountDetail, Long noticeId, String title, String content) {
        Notice notice = noticeReader.findById(noticeId);

        if (isOwner(notice, accountDetail.getAccountId())) {
            noticeSupplier.update(notice, title, content);
            return;
        }

        throw new CustomException(ExceptionStatus.NO_PERMISSION);
    }

    @Transactional
    public void delete(AccountDetail accountDetail, Long noticeId) {
        Notice notice = noticeReader.findById(noticeId);

        if (hasPermission(notice, accountDetail)) {
            noticeSupplier.delete(notice);
            return;
        }

        throw new CustomException(ExceptionStatus.NO_PERMISSION);
    }

    private boolean hasPermission(Notice notice, AccountDetail accountDetail) {
        return isOwner(notice, accountDetail.getAccountId()) || hasRole(accountDetail);
    }

    private boolean isOwner(Notice notice, Long accountId) {
        Account account = notice.getAccount();
        return account.getId().equals(accountId);
    }

    private boolean hasRole(AccountDetail accountDetail) {
        return accountDetail.hasAuthority(Role.ADMIN) || accountDetail.hasAuthority(Role.MANAGER);
    }
}
