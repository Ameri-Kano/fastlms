package com.zerobase.fastlms.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class LoginHistory {

    @Id
    @GeneratedValue()
    private int id;
    private String userId;
    private LocalDateTime loginDate;
    private String ip;
    private String userAgent;
}
