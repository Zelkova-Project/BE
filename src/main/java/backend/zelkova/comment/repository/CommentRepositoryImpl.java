package backend.zelkova.comment.repository;

import static backend.zelkova.account.entity.QAccount.account;
import static backend.zelkova.comment.entity.QComment.comment;

import backend.zelkova.comment.model.PostCommentResponse;
import backend.zelkova.comment.model.QPostCommentResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<PostCommentResponse> retrievePostComments(Long postId) {
        return jpaQueryFactory
                .select(new QPostCommentResponse(
                        account.id,
                        account.name,
                        comment.id,
                        comment.content
                ))
                .from(comment)

                .innerJoin(account)
                .on(comment.account.eq(account))

                .where(comment.post.id.eq(postId))

                .fetch();
    }
}
