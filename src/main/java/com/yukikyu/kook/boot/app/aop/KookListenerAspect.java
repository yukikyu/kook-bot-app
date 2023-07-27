package com.yukikyu.kook.boot.app.aop;

import cn.enaium.kookstarter.client.http.MessageService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.yukikyu.kook.boot.app.contexts.LocalContexts;
import com.yukikyu.kook.boot.app.domain.obj.MessageRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: yukikyu
 * @date: 2023-07-27 23:45
 */
@Slf4j
@Aspect
@Component
public class KookListenerAspect {

    @Autowired
    private MessageService messageService;

    @Pointcut("execution(* com.yukikyu.kook.boot.app.listener.KookListener.*(..))")
    public void myPointcut() {}

    @After("myPointcut()")
    public void messageAdapter() {
        List<MessageRequest> messageRequestList = LocalContexts.contextsThreadLocal.get().getMessageRequestList();
        // 发送消息
        if (CollectionUtil.isNotEmpty(messageRequestList)) {
            log.info("统一消息入口，{}", messageRequestList);
            try {
                messageRequestList.forEach(messageRequest -> {
                    Map<String, Object> stringObjectMap = BeanUtil.beanToMap(messageRequest, true, true);
                    messageService.postMessageCreate(stringObjectMap).doOnSuccess(log::info).block();
                });
            } finally {
                LocalContexts.contextsThreadLocal.get().setMessageRequestList(new ArrayList<>());
            }
        }
    }
}
