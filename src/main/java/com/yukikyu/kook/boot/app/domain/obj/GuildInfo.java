package com.yukikyu.kook.boot.app.domain.obj;

import java.util.List;
import lombok.Data;

@Data
public class GuildInfo {

    /**
     * 结果代码
     */
    private int code;

    /**
     * 操作结果消息
     */
    private String message;

    /**
     * 数据
     */
    private DataInfo data;

    @Data
    public static class DataInfo {

        /**
         * 角色列表
         */
        private List<Role> roles;

        /**
         * 频道列表
         */
        private List<Channel> channels;

        /**
         * 公会ID
         */
        private String id;

        /**
         * 公会名称
         */
        private String name;

        /**
         * 公会主题
         */
        private String topic;

        /**
         * 用户ID
         */
        private String userId;

        /**
         * 公会图标
         */
        private String icon;

        /**
         * 通知类型
         */
        private int notifyType;

        /**
         * 区域
         */
        private String region;

        /**
         * 是否开启公开
         */
        private boolean enableOpen;

        /**
         * 公开ID
         */
        private String openId;

        /**
         * 默认频道ID
         */
        private String defaultChannelId;

        /**
         * 欢迎频道ID
         */
        private String welcomeChannelId;

        /**
         * 提升数量
         */
        private int boostNum;

        /**
         * 等级
         */
        private int level;
    }

    @Data
    public static class Role {

        /**
         * 角色ID
         */
        private int roleId;

        /**
         * 角色名称
         */
        private String name;

        /**
         * 角色颜色
         */
        private int color;

        /**
         * 角色位置
         */
        private int position;

        /**
         * 是否显示成员列表置顶
         */
        private int hoist;

        /**
         * 是否可提及
         */
        private int mentionable;

        /**
         * 权限
         */
        private int permissions;
    }

    @Data
    public static class Channel {

        /**
         * 频道ID
         */
        private int id;

        /**
         * 公会ID
         */
        private String guildId;

        /**
         * 用户ID
         */
        private String userId;

        /**
         * 父频道ID
         */
        private int parentId;

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
         */
        private int type;

        /**
         * 频道等级
         */
        private int level;

        /**
         * 慢速模式
         */
        private int slowMode;

        /**
         * 是否是分类频道
         */
        private boolean isCategory;
    }
}
