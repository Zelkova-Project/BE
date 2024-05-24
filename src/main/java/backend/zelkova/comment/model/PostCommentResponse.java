package backend.zelkova.comment.model;

import com.querydsl.core.annotations.QueryProjection;

public record PostCommentResponse(Long accountId, String name, Long commentId, String comment) {

    @QueryProjection
    public PostCommentResponse {

    }
}
