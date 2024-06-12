package backend.zelkova.post.service;

import backend.zelkova.account.entity.Account;
import backend.zelkova.account.model.AccountDetail;
import backend.zelkova.account.operator.AccountReader;
import backend.zelkova.attachment.dto.response.AttachmentResponse;
import backend.zelkova.attachment.operator.AttachmentManager;
import backend.zelkova.comment.model.PostCommentResponse;
import backend.zelkova.comment.operator.CommentReader;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import backend.zelkova.post.dto.response.PostInfoResponse;
import backend.zelkova.post.dto.response.PostPreviewResponse;
import backend.zelkova.post.dto.response.PostResponse;
import backend.zelkova.post.entity.Post;
import backend.zelkova.post.model.Category;
import backend.zelkova.post.model.Visibility;
import backend.zelkova.post.operator.PostManager;
import backend.zelkova.post.operator.PostPermissionValidator;
import backend.zelkova.post.operator.PostReader;
import backend.zelkova.post.operator.PostSupplier;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final AccountReader accountReader;
    private final PostSupplier postSupplier;
    private final PostReader postReader;
    private final PostPermissionValidator postPermissionValidator;
    private final PostManager postManager;
    private final CommentReader commentReader;
    private final AttachmentManager attachmentManager;

    @Transactional
    public Long write(AccountDetail accountDetail, Category category, Visibility visibility, String title,
                      String content, List<MultipartFile> attachments) {

        Long accountId = accountDetail.getAccountId();

        if (!postPermissionValidator.hasPermission(category, accountId)) {
            throw new CustomException(ExceptionStatus.NO_PERMISSION);
        }

        Account account = accountReader.findAccountById(accountId);
        Post post = postSupplier.supply(account, category, visibility, title, content);

        Long postId = post.getId();
        attachmentManager.uploadAttachments(postId, attachments);

        return postId;
    }

    public Page<PostPreviewResponse> getPostPreviews(Pageable pageable) {
        return postReader.findAll(pageable);
    }

    public PostResponse getPost(Long postId) {
        PostInfoResponse postInfoResponse = postReader.findPostResponseByPostId(postId);
        List<AttachmentResponse> fileResponses = attachmentManager.findFiles(postId);
        List<PostCommentResponse> postComments = commentReader.findPostComments(postId);
        return new PostResponse(postInfoResponse, fileResponses, postComments);
    }

    @Transactional
    public void update(AccountDetail accountDetail, Long noticeId, Visibility visibility, String title,
                       String content, List<String> deleteAttachmentKeys, List<MultipartFile> newAttachments) {

        Post post = postReader.findById(noticeId);

        if (postPermissionValidator.isOwner(post, accountDetail.getAccountId())) {
            post.update(visibility, title, content);
            return;
        }

        attachmentManager.deleteAttachments(deleteAttachmentKeys);
        attachmentManager.uploadAttachments(post.getId(), newAttachments);

        throw new CustomException(ExceptionStatus.NO_PERMISSION);
    }

    @Transactional
    public void move(AccountDetail accountDetail, Long noticeId, Category category) {

        Post post = postReader.findById(noticeId);

        if (postPermissionValidator.hasPermission(post, accountDetail.getAccountId())) {
            post.move(category);
            return;
        }

        throw new CustomException(ExceptionStatus.NO_PERMISSION);
    }

    @Transactional
    public void delete(AccountDetail accountDetail, Long postId) {
        Post post = postReader.findById(postId);

        if (postPermissionValidator.hasPermission(post, accountDetail.getAccountId())) {
            postManager.delete(post);
            return;
        }

        attachmentManager.deleteAll(postId);

        throw new CustomException(ExceptionStatus.NO_PERMISSION);
    }
}
