package com.twilio.earthday.earth.pieceone.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowUserRequest {

    private Long userId;

    private String followerPhoneNumber;
}
