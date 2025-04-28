package com.fiveguys.fivelogbackend.domain.user.user.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SNSLinks {
    private String githubLink = "";
    private String instagramLink = "";
    private String twitterLink = "";
}
