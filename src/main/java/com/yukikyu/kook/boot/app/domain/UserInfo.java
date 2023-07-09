package com.yukikyu.kook.boot.app.domain;

import java.util.List;
import lombok.Data;

/**
 * 表示用户信息的数据对象。
 */
@Data
public class UserInfo {

    /**
     * 用户ID。
     */
    private String id;

    /**
     * 用户名。
     */
    private String username;

    /**
     * 用户昵称。
     */
    private String nickname;

    /**
     * 身份证号。
     */
    private String identifyNum;

    /**
     * 是否在线。
     */
    private boolean online;

    /**
     * 是否为机器人。
     */
    private boolean bot;

    /**
     * 用户状态。
     */
    private int status;

    /**
     * 用户横幅。
     */
    private String banner;

    /**
     * 用户头像。
     */
    private String avatar;

    /**
     * VIP用户头像。
     */
    private String vipAvatar;

    /**
     * 是否通过手机验证。
     */
    private boolean mobileVerified;

    /**
     * 加入时间。
     */
    private long joinedAt;

    /**
     * 活跃时间。
     */
    private long activeTime;

    /**
     * 用户角色列表。
     */
    private List<Integer> roles;

    /**
     * 是否为主用户。
     */
    private boolean isMaster;

    /**
     * 用户缩写。
     */
    private String abbr;

    /**
     * 用户颜色。
     */
    private int color;
}
