package backend.zelkova.post.entity;

import backend.zelkova.account.entity.Account;
import backend.zelkova.base.BaseEntity;
import backend.zelkova.post.model.Category;
import backend.zelkova.post.model.Visibility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@SoftDelete
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_code", columnDefinition = "VARCHAR(30)")
    Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_code", columnDefinition = "VARCHAR(20)")
    Visibility visibility;

    @Column(columnDefinition = "VARCHAR(255)")
    private String title;

    private String content;

    public Post(Account account, String title, String content) {
        this.account = account;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
