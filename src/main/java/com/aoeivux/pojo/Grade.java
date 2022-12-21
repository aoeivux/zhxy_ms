package com.aoeivux.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
* 
* @TableName tb_grade
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_grade")
public class Grade implements Serializable {
    @NotNull(message = "[]不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "[]不能为空")
    @Size(max = 15, message = "编码长度不能超过15")
    @Length(max = 15, message = "编码长度不能超过15")
    private String name;
    @Size(max = 15, message = "编码长度不能超过15")
    @Length(max = 15, message = "编码长度不能超过15")
    private String manager;
    @Size(max = 50, message = "编码长度不能超过50")
    @Length(max = 50, message = "编码长度不能超过50")
    private String email;
    @Size(max = 12, message = "编码长度不能超过12")
    @Length(max = 12, message = "编码长度不能超过12")
    private String telephone;
    @Size(max = 200, message = "编码长度不能超过200")
    @Length(max = 200, message = "编码长度不能超过200")
    private String introducation;
}
