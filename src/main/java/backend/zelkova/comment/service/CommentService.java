package backend.zelkova.comment.service;

import backend.zelkova.comment.entity.Comment;
import backend.zelkova.comment.operator.CommentReader;
import backend.zelkova.comment.operator.CommentSupplier;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentSupplier commentSupplier;
    private final CommentReader commentReader;

    @Transactional
    public void write(Long postId, Long accountId, String comment) {
        commentSupplier.supply(postId, accountId, comment);
    }

    @Transactional
    public void update(Long commentId, Long accountId, String content) {
        Comment comment = commentReader.findById(commentId);

        comment.updateContent(accountId, content);
    }
}
