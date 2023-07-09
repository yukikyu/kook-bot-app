package com.yukikyu.kook.boot.app.listener;

import cn.enaium.kookstarter.client.http.*;
import cn.enaium.kookstarter.event.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.yukikyu.kook.boot.app.bot.command.Match;
import com.yukikyu.kook.boot.app.constant.KookBotSettingType;
import com.yukikyu.kook.boot.app.constant.KookCommandMatchType;
import com.yukikyu.kook.boot.app.constant.KookMessageType;
import com.yukikyu.kook.boot.app.domain.Channel;
import com.yukikyu.kook.boot.app.domain.Invite;
import com.yukikyu.kook.boot.app.domain.UserInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private GuildEmojiService guildEmojiService;

    @Autowired
    private ChannelUserService channelUserService;

    @Autowired
    private InviteService inviteService;

    @Autowired
    private GuildService guildService;

    @Autowired
    private Match match;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @EventListener
    public void event(Event event) {
        log.info("事件：{}", event.getClass().getName());
        log.info("source：{}", event.getSource());
        log.info("metadata：{}", event.getMetadata());
    }

    @SneakyThrows
    @EventListener
    public void event(KMarkdownEvent event) {
        final var metaData = JSONUtil.parse(event.getMetadata().toString());
        final var author_id = metaData.getByPath("d.extra.author.id", String.class);
        final var guild_id = metaData.getByPath("d.extra.guild_id", String.class);

        // 用户指定频率（5秒一次）
        if (ObjectUtil.notEqual(author_id, "1")) {
            String key = guild_id + "::" + author_id;
            String countStr = stringRedisTemplate.opsForValue().get(key);
            int count = 0;
            if (countStr == null) {
                stringRedisTemplate.opsForValue().set(key, Integer.toString(count), 5, TimeUnit.SECONDS);
            } else {
                return;
            }
        }

        log.info("\n\n\n{}\n\n\n", metaData);

        final var data = JSONUtil.parse(metaData.getByPath("d", String.class));
        final var type = data.getByPath("channel_type", String.class);

        final var content = data.getByPath("content", String.class);
        final var bot = data.getByPath("extra.author.bot", boolean.class);
        log.info("消息内容：{}", data);

        //对方是否为机器人
        if (bot) {
            return;
        }

        String block = guildEmojiService.getGuildEmojiList(Map.of("guild_id", "7363287837020870")).block();
        log.info("222222---------------------\n{}", block);

        log.info("对方说了{}", content);
        final var chat_channel_id = data.getByPath("target_id", String.class);
        System.out.println(guild_id);
        System.out.println(author_id);
        // {"code":0,"message":"操作成功","data":{"items":[{"id":"1635770006169182","guild_id":"7363287837020870","master_id":"2431192162","parent_id":"8097939466864975","user_id":"2431192162","name":"闲聊","topic":"","type":2,"level":200,"slow_mode":0,"last_msg_content":"","last_msg_id":"","has_password":false,"limit_amount":25,"is_category":false,"permission_sync":1,"permission_overwrites":[{"role_id":0,"allow":0,"deny":0}],"permission_users":[]}],"meta":{"page":1,"page_total":1,"page_size":50,"total":1},"sort":{}}}
        List<Channel> joinedChannel = getJoinedChannel(guild_id, author_id);
        if (ObjectUtil.isEmpty(joinedChannel)) {
            return;
        }
        if (type.equals("PERSON")) {} else if (type.equals("GROUP")) {
            if (match.execute(KookCommandMatchType.FORM_A_TEAM, content)) {
                final String channel_id;
                if (joinedChannel != null) {
                    channel_id = joinedChannel.get(0).getId();
                } else {
                    return;
                }

                // 指定组队频道
                String formATeamChannelId = stringRedisTemplate
                    .opsForValue()
                    .get(guild_id + "::" + KookBotSettingType.FORM_A_TEAM_CHANNEL_ID);
                if (StrUtil.isEmpty(formATeamChannelId)) {
                    // 未指定组队频道
                    return;
                }

                // {"code":0,"message":"操作成功","data":{"url":"https://kook.top/G9ivRG","url_code":"G9ivRG"}}
                Invite invite = createInvite(channel_id);
                if (invite == null) {
                    return;
                }
                // {"s":0,"d":{"channel_type":"GROUP","type":9,"target_id":"3163216254858192","author_id":"2431192162","content":"333","extra":{"type":9,"code":"","guild_id":"7363287837020870","channel_name":"\u6d4b\u8bd5","author":{"id":"2431192162","username":"\u96ea\u7403\u4e36","identify_num":"9841","online":true,"os":"Websocket","status":1,"avatar":"https:\/\/img.kookapp.cn\/avatars\/2022-05\/4cdLRdZGbB07y07y.jpg?x-oss-process=style\/icon","vip_avatar":"https:\/\/img.kookapp.cn\/avatars\/2022-05\/4cdLRdZGbB07y07y.jpg?x-oss-process=style\/icon","banner.txt":"","nickname":"\u96ea\u7403\u4e36","roles":[],"is_vip":false,"is_ai_reduce_noise":true,"is_personal_card_bg":false,"bot":false,"decorations_id_map":null,"is_sys":false},"visible_only":null,"mention":[],"mention_all":false,"mention_roles":[],"mention_here":false,"nav_channels":[],"kmarkdown":{"raw_content":"333","mention_part":[],"mention_role_part":[],"channel_part":[]},"emoji":[],"last_msg_content":"\u96ea\u7403\u4e36\uff1a333","send_msg_device":1},"msg_id":"c285800b-e782-42b0-89cb-854208130b6f","msg_timestamp":1687969072819,"nonce":"2dJSaKesSdC1VtwSICzNvTi1","from_type":1},"extra":{"verifyToken":"4BnfeTACRFDr_mKQ","encryptKey":"Pq2sDV7oq","callbackUrl":""},"sn":1}
                messageService
                    .postMessageCreate(
                        Map.of(
                            "type",
                            KookMessageType.CARD.getValue(),
                            "target_id",
                            chat_channel_id,
                            "content",
                            StrUtil.format(
                                KookCommandMatchType.FORM_A_TEAM.getContentTemplate().get("DEFAULT"),
                                invite.getUrlCode(),
                                formATeamChannelId
                            ),
                            "quote",
                            data.getByPath("msg_id", String.class)
                        )
                    )
                    .doOnSuccess(log::info)
                    .block();
            } else if (match.execute(KookCommandMatchType.HELP, content)) {
                // 指定帮助频道
                String helpChannelId = stringRedisTemplate.opsForValue().get(guild_id + "::" + KookBotSettingType.HELP_CHANNEL_ID);
                if (StrUtil.isEmpty(helpChannelId)) {
                    // 未指定帮助频道
                    log.info("未指定帮助频道");
                    return;
                }

                // 必须在语音频道
                if (joinedChannel == null) {
                    log.info("未加入语音频道");
                    return;
                }

                messageService
                    .postMessageCreate(
                        Map.of(
                            "type",
                            KookMessageType.CARD.getValue(),
                            "target_id",
                            chat_channel_id,
                            "content",
                            KookCommandMatchType.HELP.getContentTemplate().get("ASK"),
                            "quote",
                            data.getByPath("msg_id", String.class)
                        )
                    )
                    .doOnSuccess(log::info)
                    .block();
            }
        }
    }

    @SneakyThrows
    @EventListener
    public void event(MessageBtnClick event) {
        // {"s":0,"d":{"channel_type":"PERSON","type":255,"target_id":"3950503322","author_id":"1","content":"[\u7cfb\u7edf\u6d88\u606f]","extra":{"type":"message_btn_click","body":{"value":"2222","msg_id":"41622df0-7de7-4230-80e8-afad71aeea1c","user_id":"2431192162","target_id":"7837798215125728","channel_type":"GROUP","user_info":{"id":"2431192162","username":"\u96ea\u7403\u4e36","identify_num":"9841","online":true,"os":"Websocket","status":1,"avatar":"https:\/\/img.kookapp.cn\/avatars\/2022-05\/4cdLRdZGbB07y07y.jpg?x-oss-process=style\/icon","vip_avatar":"https:\/\/img.kookapp.cn\/avatars\/2022-05\/4cdLRdZGbB07y07y.jpg?x-oss-process=style\/icon","banner":"","nickname":"\u96ea\u7403\u4e36","roles":[],"is_vip":false,"vip_amp":false,"is_ai_reduce_noise":true,"is_personal_card_bg":false,"bot":false,"decorations_id_map":{"background":10208},"mobile_verified":true,"is_sys":false,"joined_at":1686034561000,"active_time":1688893722249},"guild_id":"7363287837020870"}},"msg_id":"3899d3a0-a625-4100-b234-309453b8e0a6","msg_timestamp":1688893724725,"nonce":"","from_type":1},"extra":{"verifyToken":"4BnfeTACRFDr_mKQ","encryptKey":"Pq2sDV7oq","callbackUrl":""},"sn":4}
        log.info("metadata:{}", event.getMetadata());
        final var metaData = JSONUtil.parse(event.getMetadata().toString());
        final var msg_id = metaData.getByPath("d.extra.body.msg_id", String.class);
        final var value = metaData.getByPath("d.extra.body.value", String.class);
        final var target_id = metaData.getByPath("d.extra.body.guild_id", String.class);
        final var user_id = metaData.getByPath("d.extra.body.user_id", String.class);
        // 求助信息
        if (ObjectUtil.equals(KookCommandMatchType.HELP.name(), value)) {
            List<Channel> joinedChannel = getJoinedChannel(target_id, user_id);
            if (CollectionUtil.isEmpty(joinedChannel)) {
                return;
            }

            // 发送求助信息
            // role_id guild_id
            final var guild_id = metaData.getByPath("d.extra.body.guild_id", String.class);
            // {"code":0,"message":"操作成功","data":{"items":[{"id":"2431192162","username":"雪球丶","nickname":"雪球丶","identify_num":"9841","online":true,"bot":false,"status":1,"banner":"","avatar":"https://img.kookapp.cn/avatars/2022-05/4cdLRdZGbB07y07y.jpg?x-oss-process=style/icon","vip_avatar":"https://img.kookapp.cn/avatars/2022-05/4cdLRdZGbB07y07y.jpg?x-oss-process=style/icon","mobile_verified":true,"joined_at":1686034561000,"active_time":1688900277553,"roles":[23077198],"is_master":true,"abbr":"","color":3447003}],"meta":{"page":1,"page_total":1,"page_size":50,"total":1},"sort":{},"user_count":27,"online_count":1,"offline_count":0}}
            String guildUserListResponse = guildService
                .getGuildUserList(Map.of("guild_id", guild_id, "role_id", "23077198"))
                .doOnSuccess(System.out::println)
                .block();
            final var guildUserListResponseJson = JSONUtil.parse(guildUserListResponse);
            JSONArray guildUserListJsonArray = JSONUtil.parseArray(guildUserListResponseJson.getByPath("data.items", String.class));
            List<UserInfo> userInfoList = JSONUtil.toList(guildUserListJsonArray, UserInfo.class);

            // 获取邀请链接
            String channelId = joinedChannel.get(0).getId();
            Invite invite = createInvite(channelId, 1);
            if (invite == null) {
                return;
            }

            // 添加标记
            redisTemplate.opsForValue().set(KookCommandMatchType.HELP.name() + "::" + channelId, 0);

            // 私信发送求助信息
            List<String> msg_id_list = userInfoList
                .stream()
                .map(userInfo -> {
                    // {"code":0,"message":"操作成功","data":{"code":"b22ff8a1361f4993804d0dc6dbffbfe1","msg_id":"2d0d0182-a428-4902-979b-43a066209c71","msg_timestamp":1688924860376,"nonce":""}}
                    String postDirectMessageCreateResponse = directMessageService
                        .postDirectMessageCreate(
                            Map.of(
                                "type",
                                KookMessageType.CARD.getValue(),
                                "target_id",
                                userInfo.getId(),
                                "content",
                                StrUtil.format(KookCommandMatchType.HELP.getContentTemplate().get("HELP_MESSAGE"), invite.getUrlCode())
                            )
                        )
                        .doOnSuccess(log::info)
                        .block();
                    return JSONUtil.parse(postDirectMessageCreateResponse).getByPath("data.msg_id", String.class);
                })
                .collect(Collectors.toList());
            // 修改信息
            messageService
                .postMessageUpdate(Map.of("msg_id", msg_id, "content", KookCommandMatchType.HELP.getContentTemplate().get("WAITING")))
                .doOnSuccess(log::info)
                .block();
            redisTemplate.opsForValue().set(KookCommandMatchType.HELP.name() + "::" + channelId + "::" + user_id, 0);
            redisTemplate.opsForValue().set(KookCommandMatchType.HELP.name() + "::" + channelId + "::MESSAGE_ID_LIST", msg_id_list);
        }
    }

    @SneakyThrows
    @EventListener
    public void event(JoinedChannel event) {
        // {"s":0,"d":{"channel_type":"GROUP","type":255,"target_id":"7363287837020870","author_id":"1","content":"[\u7cfb\u7edf\u6d88\u606f]","extra":{"type":"joined_channel","body":{"user_id":"2431192162","channel_id":"5362986112653973","joined_at":1688916950984}},"msg_id":"4cdea6bb-cf8f-47d3-ad24-56ba88b23947","msg_timestamp":1688916951031,"nonce":"","from_type":1},"extra":{"verifyToken":"4BnfeTACRFDr_mKQ","encryptKey":"Pq2sDV7oq","callbackUrl":""},"sn":2}
        JSON json = JSONUtil.parse(event.getMetadata());
        String channel_id = json.getByPath("d.extra.body.channel_id", String.class);
        String key = KookCommandMatchType.HELP.name() + "::" + channel_id;
        Object help_channel_flag = redisTemplate.opsForValue().get(key);
        if (ObjectUtil.isNotNull(help_channel_flag) && (int) help_channel_flag == 0) {
            // 统计帮助时间 开始 TODO
            redisTemplate.opsForValue().set(key, 1);
            // 修改帮助信息
            // redisTemplate.opsForValue().set(KookCommandMatchType.HELP.name() + "::" + channelId + "::MESSAGE_ID_LIST", msg_id_list);
            List<String> messageIdList = (List<String>) redisTemplate
                .opsForValue()
                .get(KookCommandMatchType.HELP.name() + "::" + channel_id + "::MESSAGE_ID_LIST");
            messageIdList.forEach(msgId -> {
                directMessageService
                    .postDirectMessageUpdate(
                        Map.of("msg_id", msgId, "content", KookCommandMatchType.HELP.getContentTemplate().get("HELP_MESSAGE"))
                    )
                    .doOnSuccess(log::info)
                    .block();
            });
        }
    }

    @SneakyThrows
    @EventListener
    public void event(ExitedChannel event) {
        // {"s":0,"d":{"channel_type":"GROUP","type":255,"target_id":"7363287837020870","author_id":"1","content":"[\u7cfb\u7edf\u6d88\u606f]","extra":{"type":"joined_channel","body":{"user_id":"2431192162","channel_id":"5362986112653973","joined_at":1688916950984}},"msg_id":"4cdea6bb-cf8f-47d3-ad24-56ba88b23947","msg_timestamp":1688916951031,"nonce":"","from_type":1},"extra":{"verifyToken":"4BnfeTACRFDr_mKQ","encryptKey":"Pq2sDV7oq","callbackUrl":""},"sn":2}
        JSON json = JSONUtil.parse(event.getMetadata());
        String channel_id = json.getByPath("d.extra.body.channel_id", String.class);
        String key = KookCommandMatchType.HELP.name() + "::" + channel_id;
        Object help_channel_flag = redisTemplate.opsForValue().get(key);
        if (ObjectUtil.isNotNull(help_channel_flag) && (int) help_channel_flag > 0) {
            // 删除标记
            redisTemplate.delete(key + "*");
            // 统计帮助时间 结束 TODO
        }
    }

    /**
     * 获取加入的频道
     *
     * @param target_id 服务器id
     * @param author_id 用户id
     * @return 所在频道
     */
    private List<Channel> getJoinedChannel(String target_id, String author_id) {
        // {"code":0,"message":"操作成功","data":{"items":[{"id":"5362986112653973","guild_id":"7363287837020870","master_id":"2431192162","parent_id":"8097939466864975","user_id":"2431192162","name":"游戏","topic":"","type":2,"level":200,"slow_mode":0,"last_msg_content":"","last_msg_id":"","has_password":false,"limit_amount":25,"is_category":false,"permission_sync":1,"permission_overwrites":[{"role_id":0,"allow":0,"deny":0}],"permission_users":[]}],"meta":{"page":1,"page_total":1,"page_size":50,"total":1},"sort":{}}}
        String channelUserGetJoinedChannelResponse = channelUserService
            .getChannelUserGetJoinedChannel(Map.of("guild_id", target_id, "user_id", author_id))
            .block();
        log.info("blocked:{}", channelUserGetJoinedChannelResponse);
        final var channelList = JSONUtil.parse(channelUserGetJoinedChannelResponse).getByPath("data.items", JSONArray.class);
        if (ObjectUtil.isEmpty(channelList)) {
            return null;
        }
        return JSONUtil.toList(channelList, Channel.class);
    }

    /**
     * 创建邀请
     *
     * @param channel_id 频道id
     * @return
     */
    private Invite createInvite(String channel_id, Integer setting_times) {
        // setting_times
        Map<String, Object> params = new HashMap<>(Map.of("channel_id", channel_id));
        if (ObjectUtil.isNotNull(setting_times)) {
            params.put("setting_times", setting_times);
        }
        try {
            // {"code":0,"message":"操作成功","data":{"url":"https://kook.top/WZNhDg","url_code":"WZNhDg"}}
            final var inviteResponse = inviteService.postInviteCreate(params).block();
            JSON parse = JSONUtil.parse(inviteResponse);
            return JSONUtil.toBean(parse.getByPath("data", String.class), Invite.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建邀请
     *
     * @param channel_id 频道id
     * @return
     */
    private Invite createInvite(String channel_id) {
        return createInvite(channel_id, null);
    }
}
