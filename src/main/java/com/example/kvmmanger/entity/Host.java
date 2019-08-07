package com.example.kvmmanger.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@ApiModel(value = "主机信息", description = "主机详情信息")
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
    @Length(max = 200,message = "描述长度不能超过200")
    private String description;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date updateDate;

}
