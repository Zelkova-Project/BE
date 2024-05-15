package backend.zelkova.post.repository;

import backend.zelkova.post.dto.response.PostPreviewResponse;
import backend.zelkova.post.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostPreviewResponse> retrieveAllNoticesResponses(Pageable pageable);

    PostResponse retrieveNoticeResponse(Long noticeId);
}
