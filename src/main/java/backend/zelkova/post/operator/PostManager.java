package backend.zelkova.post.operator;

import backend.zelkova.post.entity.Post;
import backend.zelkova.post.repository.PostRepository;
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
        postRepository.delete(post);
    }
}
