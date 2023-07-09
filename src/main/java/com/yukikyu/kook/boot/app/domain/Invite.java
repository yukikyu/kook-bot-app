package com.yukikyu.kook.boot.app.domain;

import lombok.Data;

@Data
public class Invite {

    /**
     * 频道URL
     */
    private String url;

    /**
     * 频道URL代码
     */
    private String urlCode;
}
