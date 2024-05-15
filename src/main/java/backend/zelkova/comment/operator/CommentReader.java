package backend.zelkova.comment.operator;

import backend.zelkova.comment.model.PostCommentResponse;
import backend.zelkova.comment.repository.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentReader {

    private final CommentRepository commentRepository;

    public List<PostCommentResponse> findPostComments(Long postId) {
        return commentRepository.retrievePostComments(postId);
    }
}
