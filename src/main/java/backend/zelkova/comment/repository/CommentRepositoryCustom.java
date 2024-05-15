package backend.zelkova.comment.repository;

import backend.zelkova.comment.model.PostCommentResponse;
import java.util.List;

public interface CommentRepositoryCustom {

    List<PostCommentResponse> retrievePostComments(Long postId);
}
