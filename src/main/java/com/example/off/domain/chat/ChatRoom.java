package com.example.off.domain.chat;

import com.example.off.domain.project.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages = new ArrayList<>();
}
