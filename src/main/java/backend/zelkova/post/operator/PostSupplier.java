package backend.zelkova.post.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.post.entity.Post;
import backend.zelkova.post.model.Category;
import backend.zelkova.post.model.Visibility;
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

    public Post supply(Account account, Category category, Visibility visibility, String title, String content) {
        return postRepository.save(new Post(account, category, visibility, title, content));
    }
}
