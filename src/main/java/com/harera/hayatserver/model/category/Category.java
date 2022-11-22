package com.harera.hayatserver.model.category;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.harera.hayatserver.model.BaseEntity;

@Setter
@Getter
@Entity
@Table(name = "donation_category")
public class Category extends BaseEntity {

    @Column(name = "name")
    private String name;
}