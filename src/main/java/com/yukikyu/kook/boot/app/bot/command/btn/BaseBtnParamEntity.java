package com.yukikyu.kook.boot.app.bot.command.btn;

import lombok.Data;

/**
 * Base abstract class for entities which will hold definitions for created, last modified, created by,
 * last modified by attributes.
 */
@Data
public class BaseBtnParamEntity {

    // 类型
    private String type;

    // 作者ID
    private String authorId = "1";

    // 服务器ID
    private String guildId;
}
