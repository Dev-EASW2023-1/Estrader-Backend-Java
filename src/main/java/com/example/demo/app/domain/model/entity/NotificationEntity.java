package com.example.demo.app.domain.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private boolean isCompleted;

    @Builder
    public NotificationEntity(
            ItemEntity item,
            UserEntity user,
            String region,
            boolean isCompleted
    ) {
        this.item = item;
        this.user = user;
        this.region = region;
        this.isCompleted = isCompleted;
    }
}
