package backend.zelkova.post.dto.response;

import backend.zelkova.comment.model.PostCommentResponse;
import java.util.List;

public record PostAndCommentResponse(PostResponse postResponse, List<PostCommentResponse> postCommentResponses) {
}
