package com.tuan.springbootfinal.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.util.Date;
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    private String updateBy;
    @CreationTimestamp
    private Date updateAt;
    @UpdateTimestamp
    private Date createAt;
}
