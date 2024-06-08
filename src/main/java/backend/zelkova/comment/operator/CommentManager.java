package backend.zelkova.comment.operator;

import backend.zelkova.comment.entity.Comment;
import backend.zelkova.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentManager {

    private final CommentRepository commentRepository;

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
