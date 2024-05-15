package backend.zelkova.notice.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import backend.zelkova.notice.entity.Notice;
import backend.zelkova.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class NoticeSupplier {

    private final NoticeRepository noticeRepository;

    public Notice supply(Account account, String title, String content) {
        Notice notice = new Notice(account, title, content);
        return noticeRepository.save(notice);
    }

    public void update(Notice notice, Long accountId, String title, String content) {
        Account noticeOwner = notice.getAccount();

        if (noticeOwner.getId().equals(accountId)) {
            notice.update(title, content);
            return;
        }

        throw new CustomException(ExceptionStatus.NO_PERMISSION);
    }
}
