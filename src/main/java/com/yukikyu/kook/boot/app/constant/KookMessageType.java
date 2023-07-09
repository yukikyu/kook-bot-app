package com.yukikyu.kook.boot.app.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yukikyu
 * @date: 2023-07-08 19:51
 */
@Getter
@AllArgsConstructor
public enum KookMessageType {
    /**
     * 文本
     */
    TEXT(1),
    /**
     * KMARKDOWN
     */
    KMARKDOWN(9),
    /**
     * 卡片
     */
    CARD(10);

    private final int value;
}
