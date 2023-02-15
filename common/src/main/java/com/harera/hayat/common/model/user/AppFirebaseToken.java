package com.harera.hayat.common.model.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppFirebaseToken {

    private String uid;
    private String email;
    private String name;
    private String picture;
}