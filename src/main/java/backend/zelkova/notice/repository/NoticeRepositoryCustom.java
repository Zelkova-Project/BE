package backend.zelkova.notice.repository;

import backend.zelkova.notice.dto.response.NoticePreviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<NoticePreviewResponse> retrieveAllNoticesResponses(Pageable pageable);
}
