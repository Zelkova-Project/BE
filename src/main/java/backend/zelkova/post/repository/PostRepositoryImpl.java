package backend.zelkova.post.repository;

import static backend.zelkova.post.entity.QPost.post;

import backend.zelkova.post.dto.response.PostPreviewResponse;
import backend.zelkova.post.dto.response.PostResponse;
import backend.zelkova.post.dto.response.QPostPreviewResponse;
import backend.zelkova.post.dto.response.QPostResponse;
import backend.zelkova.post.entity.QPost;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<PostPreviewResponse> retrieveAllNoticesResponses(Pageable pageable) {
        List<PostPreviewResponse> content = jpaQueryFactory
                .select(new QPostPreviewResponse(
                        post.id,
                        post.title,
                        post.createdAt
                ))

                .from(post)

                .orderBy(post.id.desc())

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(post.count())
                .from(post);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public PostResponse retrieveNoticeResponse(Long postId) {

        QPost prev = new QPost("prev");
        QPost next = new QPost("next");

        return jpaQueryFactory
                .select(new QPostResponse(
                        post.id,
                        post.title,
                        post.content,
                        post.createdAt,

                        new QPostPreviewResponse(
                                prev.id,
                                prev.title,
                                prev.createdAt
                        ),

                        new QPostPreviewResponse(
                                next.id,
                                next.title,
                                next.createdAt
                        )
                ))

                .from(post)

                .leftJoin(prev)
                .on(prev.id.eq(
                        jpaQueryFactory
                                .select(post.id.max())
                                .from(post)
                                .where(post.id.lt(postId))
                ))

                .leftJoin(next)
                .on(next.id.eq(
                        jpaQueryFactory
                                .select(post.id.min())
                                .from(post)
                                .where(post.id.gt(postId))
                ))

                .where(post.id.eq(postId))

                .fetchOne();
    }
}
