package com.yukikyu.kook.boot.app.listener;

import cn.enaium.kookstarter.client.http.*;
import cn.enaium.kookstarter.event.Event;
import cn.enaium.kookstarter.event.KMarkdownEvent;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yukikyu.kook.boot.app.bot.command.Match;
import com.yukikyu.kook.boot.app.constant.KookCommandMatchType;
import com.yukikyu.kook.boot.app.constant.KookMessageType;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
    GuildEmojiService guildEmojiService;

    @Autowired
    ChannelUserService channelUserService;

    @Autowired
    InviteService inviteService;

    @Autowired
    private Match match;

    @EventListener
    public void event(Event event) {
        log.info("事件：{}", event.getClass().getName());
        log.info("source：{}", event.getSource());
        log.info("metadata：{}", event.getMetadata());
    }

    @EventListener
    public void event(KMarkdownEvent event) throws JsonProcessingException {
        final var metaData = JSONUtil.parse(event.getMetadata().toString());

        log.info("\n\n\n{}\n\n\n", metaData);

        final var data = JSONUtil.parse(metaData.getByPath("d", String.class));
        final var type = data.getByPath("channel_type", String.class);

        final var content = data.getByPath("content", String.class);
        final var bot = data.getByPath("extra.author.bot", boolean.class);
        log.info("消息内容：{}", data);

        if (bot) { //对方是否为机器人
            return;
        }

        String block = guildEmojiService.getGuildEmojiList(Map.of("guild_id", "7363287837020870")).block();
        log.info("222222---------------------\n{}", block);

        log.info("对方说了{}", content);
        final var target_id = data.getByPath("extra.guild_id", String.class);
        final var author_id = data.getByPath("extra.author.id", String.class);
        final var chat_channel_id = data.getByPath("target_id", String.class);
        System.out.println(target_id);
        System.out.println(author_id);
        // {"code":0,"message":"操作成功","data":{"items":[{"id":"1635770006169182","guild_id":"7363287837020870","master_id":"2431192162","parent_id":"8097939466864975","user_id":"2431192162","name":"闲聊","topic":"","type":2,"level":200,"slow_mode":0,"last_msg_content":"","last_msg_id":"","has_password":false,"limit_amount":25,"is_category":false,"permission_sync":1,"permission_overwrites":[{"role_id":0,"allow":0,"deny":0}],"permission_users":[]}],"meta":{"page":1,"page_total":1,"page_size":50,"total":1},"sort":{}}}
        String channelUserGetJoinedChannelResponse = channelUserService
            .getChannelUserGetJoinedChannel(Map.of("guild_id", target_id, "user_id", author_id))
            .block();
        log.info("blocked:{}", channelUserGetJoinedChannelResponse);
        final var channelUserGetJoinedChannelResponseJson = JSONUtil.parse(channelUserGetJoinedChannelResponse);
        if (type.equals("PERSON")) {} else if (type.equals("GROUP")) {
            if (match.execute(KookCommandMatchType.FORM_A_TEAM, content)) {
                final var channel_id = channelUserGetJoinedChannelResponseJson.getByPath("data.items[0].id", String.class);
                // {"code":0,"message":"操作成功","data":{"url":"https://kook.top/G9ivRG","url_code":"G9ivRG"}}
                final var inviteResponse = inviteService.postInviteCreate(Map.of("channel_id", channel_id)).block();
                final var inviteResponseJson = JSONUtil.parse(inviteResponse);
                final var invite_code = inviteResponseJson.getByPath("data.url_code", String.class);
                log.info("inviteResponse:{}", inviteResponse);
                // {"s":0,"d":{"channel_type":"GROUP","type":9,"target_id":"3163216254858192","author_id":"2431192162","content":"333","extra":{"type":9,"code":"","guild_id":"7363287837020870","channel_name":"\u6d4b\u8bd5","author":{"id":"2431192162","username":"\u96ea\u7403\u4e36","identify_num":"9841","online":true,"os":"Websocket","status":1,"avatar":"https:\/\/img.kookapp.cn\/avatars\/2022-05\/4cdLRdZGbB07y07y.jpg?x-oss-process=style\/icon","vip_avatar":"https:\/\/img.kookapp.cn\/avatars\/2022-05\/4cdLRdZGbB07y07y.jpg?x-oss-process=style\/icon","banner.txt":"","nickname":"\u96ea\u7403\u4e36","roles":[],"is_vip":false,"is_ai_reduce_noise":true,"is_personal_card_bg":false,"bot":false,"decorations_id_map":null,"is_sys":false},"visible_only":null,"mention":[],"mention_all":false,"mention_roles":[],"mention_here":false,"nav_channels":[],"kmarkdown":{"raw_content":"333","mention_part":[],"mention_role_part":[],"channel_part":[]},"emoji":[],"last_msg_content":"\u96ea\u7403\u4e36\uff1a333","send_msg_device":1},"msg_id":"c285800b-e782-42b0-89cb-854208130b6f","msg_timestamp":1687969072819,"nonce":"2dJSaKesSdC1VtwSICzNvTi1","from_type":1},"extra":{"verifyToken":"4BnfeTACRFDr_mKQ","encryptKey":"Pq2sDV7oq","callbackUrl":""},"sn":1}
                messageService
                    .postMessageCreate(
                        Map.of(
                            "type",
                            KookMessageType.CARD.getValue(),
                            "target_id",
                            chat_channel_id,
                            "content",
                            StrUtil.format(KookCommandMatchType.FORM_A_TEAM.getContent(), invite_code, ""),
                            "quote",
                            data.getByPath("msg_id", String.class)
                        )
                    )
                    .doOnSuccess(log::info)
                    .block();
                // 频道
                /*messageService.postMessageCreate(
                        Map.of(
                                "type", 9,
                                "target_id", "7837798215125728",
                                "content", "23"
                        )).block();*/
            }
        }
    }
}
