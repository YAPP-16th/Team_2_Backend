package com.yapp.ios2.vo;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "albumOwner")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class AlbumOwner {
    @Id
    @GeneratedValue
    private Long uid;

    @Column
    private Long albumUid;

    @Column
    private Long userUid;

    @Column
    private String role;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    public AlbumOwner() {
    }

    public AlbumOwner(Long userUid, String role) {
        this.userUid = userUid;
        this.role = role;
    }
}