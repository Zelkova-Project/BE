package backend.zelkova.comment.service;

import backend.zelkova.comment.operator.CommentSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentSupplier commentSupplier;

    @Transactional
    public void write(Long postId, Long accountId, String comment) {
        commentSupplier.supply(postId, accountId, comment);
    }
}
