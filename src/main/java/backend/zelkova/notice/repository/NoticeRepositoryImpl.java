package backend.zelkova.notice.repository;

import static backend.zelkova.notice.entity.QNotice.notice;

import backend.zelkova.notice.dto.response.NoticePreviewResponse;
import backend.zelkova.notice.dto.response.QNoticePreviewResponse;
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
}
