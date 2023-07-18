package com.yukikyu.kook.boot.app.domain.factory;

import com.yukikyu.kook.boot.app.constant.KookMessageType;
import com.yukikyu.kook.boot.app.domain.obj.MessageRequest;
import org.springframework.stereotype.Component;

/**
 * Kook消息请求工厂
 *
 * @author: yukikyu
 * @date: 2023-07-18 16:11
 */
@Component
public class MessageRequestFactory {

    /**
     * 成功
     *
     * @param quote
     * @param tempTargetId
     * @return
     */
    public static MessageRequest success(String quote, String tempTargetId) {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setContent("成功");
        messageRequest.setType(KookMessageType.KMARKDOWN.getValue());
        messageRequest.setQuote(quote);
        messageRequest.setTempTargetId(tempTargetId);
        return messageRequest;
    }
}
