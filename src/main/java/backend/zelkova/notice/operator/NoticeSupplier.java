package backend.zelkova.notice.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.notice.entity.Notice;
import backend.zelkova.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticeSupplier {

    private final NoticeRepository noticeRepository;

    public Notice supply(Account account, String title, String content) {
        Notice notice = new Notice(account, title, content);
        return noticeRepository.save(notice);
    }
}
