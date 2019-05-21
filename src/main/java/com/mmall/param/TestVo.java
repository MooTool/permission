package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
@Getter
@Setter
public class TestVo {

    @NotBlank(message = "msg不能为空")
    private String  msg;
    @NotNull
    private Integer id;
}
