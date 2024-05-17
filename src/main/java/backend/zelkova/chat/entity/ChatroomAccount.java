package backend.zelkova.chat.entity;

import backend.zelkova.account.entity.Account;
import backend.zelkova.base.BaseEntity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chatroom_accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomAccount extends BaseEntity {

    @EmbeddedId
    private ChatroomAccountId id;

    @MapsId("chatroomId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    @MapsId("accountId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ChatroomAccountId implements Serializable {
        private Long chatroomId;
        private Long accountId;
    }

    public ChatroomAccount(Chatroom chatroom, Account account) {
        this.id = new ChatroomAccountId(chatroom.getId(), account.getId());
        this.chatroom = chatroom;
        this.account = account;
    }
}
