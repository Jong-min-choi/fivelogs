package com.fiveguys.fivelogbackend.domain.user.user.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SNSLinks {
    private String githubLink;
    private String instagramLink;
    private String twitterLink;
}
