package backend.zelkova.comment.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.repository.AccountRepository;
import backend.zelkova.comment.entity.Comment;
import backend.zelkova.comment.repository.CommentRepository;
import backend.zelkova.post.entity.Post;
import backend.zelkova.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class CommentSupplier {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;

    public Comment supply(Long postId, Long accountId, String comment) {
        Post post = postRepository.getReferenceById(postId);
        Account account = accountRepository.getReferenceById(accountId);
        return commentRepository.save(new Comment(post, account, comment));
    }
}
