package backend.zelkova.post.operator;

import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import backend.zelkova.post.dto.response.PostPreviewResponse;
import backend.zelkova.post.dto.response.PostResponse;
import backend.zelkova.post.entity.Post;
import backend.zelkova.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostReader {

    private final PostRepository postRepository;

    public Page<PostPreviewResponse> findAll(Pageable pageable) {
        return postRepository.retrieveAllNoticesResponses(pageable);
    }

    public PostResponse findNoticeResponseByNoticeId(Long noticeId) {
        return postRepository.retrieveNoticeResponse(noticeId);
    }

    public Post findById(Long noticeId) {
        return postRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.NOTFOUND));
    }
}
