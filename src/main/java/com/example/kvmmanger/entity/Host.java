package com.example.kvmmanger.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Host {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "名称不能为空")
    @Length(max = 50,message = "名称长度不能超过50")
    private String name;
    @NotBlank(message = "IP不能为空")
    @Length(max = 100,message = "IP长度不能超过100")
    private String ip;
    @NotBlank(message = "账号不能为空")
    @Length(max = 50,message = "账号长度不能超过50")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Length(max = 50,message = "密码长度不能超过50")
    private String password;
}
