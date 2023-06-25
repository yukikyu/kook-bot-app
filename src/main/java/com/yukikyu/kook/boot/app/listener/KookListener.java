package com.yukikyu.kook.boot.app.listener;

import cn.enaium.kookstarter.client.http.DirectMessageService;
import cn.enaium.kookstarter.client.http.MessageService;
import cn.enaium.kookstarter.client.http.UserService;
import cn.enaium.kookstarter.event.Event;
import cn.hutool.core.util.EnumUtil;
import com.yukikyu.kook.boot.app.constant.KookEventType;
import com.yukikyu.kook.boot.app.domain.EventContent;
import com.yukikyu.kook.boot.app.repository.EventContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Kook事件监听
 *
 * @author: yukikyu
 * @date: 2023-06-19 18:27
 */
@Slf4j
@Component
public class KookListener {

    @Autowired
    private MessageService messageService;

    @Autowired
    private DirectMessageService directMessageService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventContentRepository eventContentRepository;

    @EventListener
    public Mono<EventContent> event(Event event) {
        EventContent eventContent = new EventContent();
        String name = event.getClass().getSimpleName();
        eventContent.setName(name);
        String title = EnumUtil.getFieldBy(KookEventType::getTitle, KookEventType::getName, name);
        eventContent.setTitle(title);
        eventContent.setClassName(event.getClass().getName());
        String metadata = event.getMetadata().toString();
        log.info("metadata：{}", metadata);
        eventContent.setMetadata(metadata);
        Mono<EventContent> eventContentMono = eventContentRepository.save(eventContent);
        return eventContentMono;
    }
    /*@EventListener
    public void event(KMarkdownEvent event) throws JsonProcessingException {
        final var jsonNode = new ObjectMapper().readValue(event.getMetadata().toString(), JsonNode.class);
        final var data = jsonNode.get("d");
        final var type = data.get("channel_type").textValue();
        final var content = data.get("extra").get("kmarkdown").get("raw_content").textValue();
        final var bot = data.get("extra").get("author").get("bot").booleanValue();

        log.info("消息内容：{}", data);

        if (bot) {//对方是否为机器人
            return;
        }

        log.info("对方说了{}", content);
        if (type.equals("PERSON")) {
            directMessageService.postDirectMessageCreate(
                    Map.of(
                            "type", 9,
                            "target_id", data.get("author_id"),
                            "content", "**Hello**"
                    )
            ).doOnSuccess(result -> {
                log.info("请求成功:{}", result);
            }).doOnError(throwable -> {
                log.error("请求错误");
                throwable.printStackTrace();
            }).subscribe();


        } else if (type.equals("GROUP")) {
            messageService.postMessageCreate(
                    Map.of(
                            "type", 9,
                            "target_id", data.get("target_id"),
                            "content", "**Hello**"
                    )
            ).doOnSuccess(result -> {
                log.info("请求成功:{}", result);
            }).doOnError(throwable -> {
                log.error("请求错误");
                throwable.printStackTrace();
            }).block();
        }
    }*/

}
