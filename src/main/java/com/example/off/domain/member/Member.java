package com.example.off.domain.member;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.chat.ChatRoomMember;
import com.example.off.domain.chat.Message;
import com.example.off.domain.notification.Notification;
import com.example.off.domain.partnerRecruit.PartnerApplication;
import com.example.off.domain.pay.PayLog;
import com.example.off.domain.project.Project;
import com.example.off.domain.projectMember.ProjectMember;
import com.example.off.domain.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    public static final int NICKNAME_MAX_LENGTH = 50;
    public static final int SELF_INTRO_MAX_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = NICKNAME_MAX_LENGTH)
    private String nickname;

    @Column(nullable = false, length = SELF_INTRO_MAX_LENGTH)
    private String selfIntroduction;

    @Column(nullable = false)
    private LocalDate birth;

    @Setter
    @Column(nullable = false, length = 500)
    private String profileImage;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean isWorking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectCountType projectCountType;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolios = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PartnerApplication> partnerApplications = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PayLog> payLogs = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Project> projects = new ArrayList<>();

    private Member(
            String name, String email, String password, String nickname, Role role, String selfIntroduction, LocalDate birth, ProjectCountType projectCountType, String profileImage
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.selfIntroduction = selfIntroduction;
        this.birth = birth;
        this.isWorking = false;
        this.projectCountType = projectCountType;
        this.profileImage = profileImage;
    }

    public static Member of(String name, String email, String password, String nickname, Role role, String selfIntroduction, LocalDate birth, ProjectCountType projectCountType, String profileImage) {
        return new Member(
                name, email, password, nickname, role, selfIntroduction, birth, projectCountType, profileImage
        );
    }

    public void addPortfolio(Portfolio portfolio){
        this.portfolios.add(portfolio);
        portfolio.setMember(this); //FK 설정
    }

    //Setter
    public void updateNickname(String nickname) { this.nickname = nickname; }
    public void updateProjectCount(ProjectCountType count){
        this.projectCountType = count;
    }
    public void updateIntroduction(String selfIntroduction) { this.selfIntroduction = selfIntroduction; }
    public void updateWorking(Boolean isWorking) { this.isWorking = isWorking; }
}
