package com.yukikyu.kook.boot.app.domain.obj;

import com.yukikyu.kook.boot.app.constant.KookMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    /**
     * 消息类型，默认为1，代表文本类型。
     * 可选值：1 - 文本类型，9 - KMarkdown消息，10 - 卡片消息。
     */
    private Integer type = KookMessageType.TEXT.getValue();

    /**
     * 目标频道ID，必需
     */
    private String targetId;

    /**
     * 消息内容，必需
     */
    private String content;

    /**
     * 回复某条消息的msgId，可选
     */
    private String quote;

    /**
     * nonce，服务端不做处理，原样返回，可选
     */
    private String nonce;

    /**
     * 用户ID，如果传了，代表该消息是临时消息，该消息不会存入数据库，但会在频道内只给该用户推送临时消息。
     * 用于在频道内针对用户的操作进行单独的回应通知等。
     * 可选
     */
    private String tempTargetId;
}
