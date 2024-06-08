package backend.zelkova.post.dto.response;

import backend.zelkova.attachment.dto.response.AttachmentResponse;
import backend.zelkova.comment.model.PostCommentResponse;
import java.util.List;

public record PostResponse(PostInfoResponse postInfoResponse, List<AttachmentResponse> attachmentResponses,
                           List<PostCommentResponse> postCommentResponses) {
}
