package backend.zelkova.post.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.post.entity.Post;
import backend.zelkova.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class PostSupplier {

    private final PostRepository postRepository;

    public Post supply(Account account, String title, String content) {
        Post post = new Post(account, title, content);
        return postRepository.save(post);
    }

    public void update(Post post, String title, String content) {
        post.update(title, content);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }
}
