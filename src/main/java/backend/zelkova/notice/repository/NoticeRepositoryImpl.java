package backend.zelkova.notice.repository;

import static backend.zelkova.notice.entity.QNotice.notice;

import backend.zelkova.notice.dto.response.NoticePreviewResponse;
import backend.zelkova.notice.dto.response.NoticeResponse;
import backend.zelkova.notice.dto.response.QNoticePreviewResponse;
import backend.zelkova.notice.dto.response.QNoticeResponse;
import backend.zelkova.notice.entity.QNotice;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<NoticePreviewResponse> retrieveAllNoticesResponses(Pageable pageable) {
        List<NoticePreviewResponse> content = jpaQueryFactory
                .select(new QNoticePreviewResponse(
                        notice.id,
                        notice.title,
                        notice.createdAt
                ))

                .from(notice)

                .orderBy(notice.id.desc())

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(notice.count())
                .from(notice);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public NoticeResponse retrieveNoticeResponse(Long noticeId) {

        QNotice prev = new QNotice("prev");
        QNotice next = new QNotice("next");

        return jpaQueryFactory
                .select(new QNoticeResponse(
                        notice.id,
                        notice.title,
                        notice.content,
                        notice.createdAt,

                        new QNoticePreviewResponse(
                                prev.id,
                                prev.title,
                                prev.createdAt
                        ),

                        new QNoticePreviewResponse(
                                next.id,
                                next.title,
                                next.createdAt
                        )
                ))

                .from(notice)

                .leftJoin(prev)
                .on(prev.id.eq(
                        jpaQueryFactory
                                .select(notice.id.max())
                                .from(notice)
                                .where(notice.id.lt(noticeId))
                ))

                .leftJoin(next)
                .on(next.id.eq(
                        jpaQueryFactory
                                .select(notice.id.min())
                                .from(notice)
                                .where(notice.id.gt(noticeId))
                ))

                .where(notice.id.eq(noticeId))

                .fetchOne();
    }
}
