package com.yukikyu.kook.boot.app.domain.obj;

import lombok.Data;

@Data
public class Channel {

    /**
     * 频道ID
     */
    private String id;

    /**
     * 服务器ID
     */
    private String guildId;

    /**
     * 频道管理员ID
     */
    private String masterId;

    /**
     * 父频道ID
     */
    private String parentId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 频道名称
     */
    private String name;

    /**
     * 频道主题
     */
    private String topic;

    /**
     * 频道类型
     * 1 - 文字频道
     * 2 - 语音频道
     */
    private int type;

    /**
     * 频道等级
     */
    private int level;

    /**
     * 慢速模式时间（秒）
     */
    private int slowMode;

    /**
     * 最后一条消息内容
     */
    private String lastMsgContent;

    /**
     * 最后一条消息ID
     */
    private String lastMsgId;

    /**
     * 是否有密码
     */
    private boolean hasPassword;

    /**
     * 限制人数
     */
    private int limitAmount;

    /**
     * 是否为分类频道
     */
    private boolean isCategory;

    /**
     * 权限同步
     * 0 - 不同步
     * 1 - 同步
     */
    private int permissionSync;

    /**
     * 权限覆盖
     */
    private PermissionOverwrite[] permissionOverwrites;

    /**
     * 权限用户
     */
    private String[] permissionUsers;

    @Data
    public static class PermissionOverwrite {

        /**
         * 角色ID
         */
        private int roleId;

        /**
         * 允许权限
         */
        private int allow;

        /**
         * 拒绝权限
         */
        private int deny;
    }
}
