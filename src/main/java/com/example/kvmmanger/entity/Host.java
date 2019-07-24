package com.example.kvmmanger.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Host {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String ip;
    private String username;
    private String password;
}
