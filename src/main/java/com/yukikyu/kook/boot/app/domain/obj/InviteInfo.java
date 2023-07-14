package com.yukikyu.kook.boot.app.domain.obj;

import java.util.List;
import lombok.Data;

@Data
public class InviteInfo {

    /**
     * ID
     */
    private int id;

    /**
     * 频道ID
     */
    private String channelId;

    /**
     * 公会ID
     */
    private String guildId;

    /**
     * 公会名称
     */
    private String guildName;

    /**
     * 频道名称
     */
    private String channelName;

    /**
     * 类型
     */
    private int type;

    /**
     * 频道URL代码
     */
    private String urlCode;

    /**
     * 频道URL
     */
    private String url;

    /**
     * 用户信息
     */
    private UserInfo user;

    /**
     * 过期时间
     */
    private long expireTime;

    /**
     * 剩余次数
     */
    private int remainingTimes;

    /**
     * 使用次数
     */
    private int usingTimes;

    /**
     * 持续时间
     */
    private int duration;

    @Data
    public static class UserInfo {

        /**
         * 用户ID
         */
        private String id;

        /**
         * 用户名
         */
        private String username;

        /**
         * 身份号码
         */
        private String identifyNum;

        /**
         * 在线状态
         */
        private boolean online;

        /**
         * 操作系统
         */
        private String os;

        /**
         * 用户状态
         */
        private int status;

        /**
         * 头像
         */
        private String avatar;

        /**
         * VIP头像
         */
        private String vipAvatar;

        /**
         * 横幅
         */
        private String banner;

        /**
         * 昵称
         */
        private String nickname;

        /**
         * 角色列表
         */
        private List<Integer> roles;

        /**
         * 是否VIP
         */
        private boolean isVip;

        /**
         * VIP加强版
         */
        private boolean vipAmp;

        /**
         * 是否启用AI降噪
         */
        private boolean isAiReduceNoise;

        /**
         * 是否使用个人名片背景
         */
        private boolean isPersonalCardBg;

        /**
         * 是否是机器人
         */
        private boolean bot;

        /**
         * 装饰物ID映射
         */
        private Object decorationsIdMap;

        /**
         * 机器人状态
         */
        private int botStatus;

        /**
         * 标签信息
         */
        private TagInfo tagInfo;

        /**
         * 手机验证
         */
        private boolean mobileVerified;

        /**
         * 是否是系统用户
         */
        private boolean isSys;

        /**
         * 客户端ID
         */
        private String clientId;

        /**
         * 加入时间
         */
        private long joinedAt;

        /**
         * 活跃时间
         */
        private long activeTime;

        @Data
        public static class TagInfo {

            /**
             * 颜色
             */
            private String color;

            /**
             * 背景颜色
             */
            private String bgColor;

            /**
             * 文本
             */
            private String text;
        }
    }
}
