package com.harera.hayat.framework.model.user;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.harera.hayat.framework.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_")
public class User extends BaseEntity {

    @Basic
    @Column(name = "phone_number")
    private String mobile;

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    private String lastName;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "uid")
    private String uid;

    @Column(name = "device_token")
    private String deviceToken;
}
