package com.yukikyu.kook.boot.app.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yukikyu
 * @date: 2023-07-09 23:52
 */
@Getter
@AllArgsConstructor
public enum KookBotSettingType {
    /**
     * 帮助频道ID
     */
    HELP_CHANNEL_ID,
    /**
     * 组队频道ID
     */
    FORM_A_TEAM_CHANNEL_ID,
    /**
     * 帮助人角色ID
     */
    HELP_ROLE_ID;

    @Override
    public String toString() {
        return this.name();
    }
}
