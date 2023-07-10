package com.yukikyu.kook.boot.app.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yukikyu
 * @date: 2023-07-11 0:24
 */
@Getter
@AllArgsConstructor
public enum HelpLogStatus {
    STARTING("开始"),
    ENDED("结束");

    final String title;

    @Override
    public String toString() {
        return this.name();
    }
}
