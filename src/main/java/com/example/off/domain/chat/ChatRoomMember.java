package com.example.off.domain.chat;

import com.example.off.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "chat_room_member",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_member_chat_room", columnNames = {"member_id", "chat_room_id"})
        }
)
public class ChatRoomMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_member_id")
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime lastReadAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    public void updateLastReadAt() {
        this.lastReadAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.lastReadAt == null) {
            this.lastReadAt = LocalDateTime.now();
        }
    }

    public ChatRoomMember(ChatRoom chatRoom, Member member) {
        this.chatRoom = chatRoom;
        this.member = member;
    }
}
