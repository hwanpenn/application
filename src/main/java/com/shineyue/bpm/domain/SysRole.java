package com.shineyue.bpm.domain;


import javax.persistence.*;

/**
 * 角色类
 *
 * @author liubo
 * @version 2017-12-15 14:33
 **/
@Entity
@Table(name="sys_role")
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
