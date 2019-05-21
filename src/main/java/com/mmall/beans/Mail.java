package com.mmall.beans;

import lombok.*;

import java.util.Set;

/**
 * Created by wcc on 2019\5\21 0021 15:37
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private String  subject;//主题

    private String message;

    private Set<String> receivers;
}
