package backend.zelkova.post.repository;

import backend.zelkova.post.dto.response.PostInfoResponse;
import backend.zelkova.post.dto.response.PostPreviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostPreviewResponse> retrieveAllPostPreviewResponses(Pageable pageable);

    PostInfoResponse retrievePostResponse(Long noticeId);
}
