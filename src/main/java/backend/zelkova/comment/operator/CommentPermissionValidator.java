package backend.zelkova.comment.operator;

import backend.zelkova.account.entity.Account;
import backend.zelkova.comment.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentPermissionValidator {

    public boolean isOwner(Comment comment, Long accountId) {
        Account account = comment.getAccount();
        return account.getId().equals(accountId);
    }
}
