package backend.zelkova.post.operator;

import backend.zelkova.comment.entity.Comment;
import backend.zelkova.post.entity.Post;
import backend.zelkova.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class PostManager {

    private final PostRepository postRepository;

    public void delete(Post post) {
        List<Comment> comments = post.getComments();
        comments.forEach(Comment::breakPostRelation);

        postRepository.delete(post);
    }
}
