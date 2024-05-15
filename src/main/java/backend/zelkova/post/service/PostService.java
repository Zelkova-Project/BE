package backend.zelkova.post.service;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.account.operator.AccountReader;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import backend.zelkova.post.dto.response.PostPreviewResponse;
import backend.zelkova.post.dto.response.PostResponse;
import backend.zelkova.post.entity.Post;
import backend.zelkova.post.operator.PostPermissionValidator;
import backend.zelkova.post.operator.PostReader;
import backend.zelkova.post.operator.PostSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final AccountReader accountReader;
    private final PostSupplier postSupplier;
    private final PostReader postReader;
    private final PostPermissionValidator postPermissionValidator;

    @Transactional
    public Long write(AccountDetail accountDetail, String title, String content) {
        Account account = accountReader.findAccountById(accountDetail.getAccountId());
        Post post = postSupplier.supply(account, title, content);
        return post.getId();
    }

    public Page<PostPreviewResponse> getPostPreviews(Pageable pageable) {
        return postReader.findAll(pageable);
    }

    public PostResponse getPost(Long noticeId) {
        return postReader.findPostResponseByPostId(noticeId);
    }

    @Transactional
    public void update(AccountDetail accountDetail, Long noticeId, String title, String content) {
        Post post = postReader.findById(noticeId);

        if (postPermissionValidator.isOwner(post, accountDetail.getAccountId())) {
            postSupplier.update(post, title, content);
            return;
        }

        throw new CustomException(ExceptionStatus.NO_PERMISSION);
    }

    @Transactional
    public void delete(AccountDetail accountDetail, Long noticeId) {
        Post post = postReader.findById(noticeId);

        if (postPermissionValidator.hasPermission(post, accountDetail.getAccountId())) {
            postSupplier.delete(post);
            return;
        }

        throw new CustomException(ExceptionStatus.NO_PERMISSION);
    }
}
