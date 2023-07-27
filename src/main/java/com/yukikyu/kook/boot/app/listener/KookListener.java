package com.yukikyu.kook.boot.app.listener;

import cn.enaium.kookstarter.client.http.*;
import cn.enaium.kookstarter.event.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.yukikyu.kook.boot.app.bot.command.KookCommandMatch;
import com.yukikyu.kook.boot.app.bot.command.btn.BaseBtnParamEntity;
import com.yukikyu.kook.boot.app.bot.command.btn.HelpBtnParam;
import com.yukikyu.kook.boot.app.business.HelpStatisticsBusiness;
import com.yukikyu.kook.boot.app.config.Constants;
import com.yukikyu.kook.boot.app.constant.HelpLogStatus;
import com.yukikyu.kook.boot.app.constant.KookBotSettingType;
import com.yukikyu.kook.boot.app.constant.KookCommandMatchType;
import com.yukikyu.kook.boot.app.constant.KookMessageType;
import com.yukikyu.kook.boot.app.contexts.LocalContexts;
import com.yukikyu.kook.boot.app.domain.HelpStatistics;
import com.yukikyu.kook.boot.app.domain.HelpUserLog;
import com.yukikyu.kook.boot.app.domain.UserInfo;
import com.yukikyu.kook.boot.app.domain.criteria.HelpStatisticsCriteria;
import com.yukikyu.kook.boot.app.domain.factory.MessageRequestFactory;
import com.yukikyu.kook.boot.app.domain.obj.Channel;
import com.yukikyu.kook.boot.app.domain.obj.GuildInfo;
import com.yukikyu.kook.boot.app.domain.obj.Invite;
import com.yukikyu.kook.boot.app.domain.obj.MessageRequest;
import com.yukikyu.kook.boot.app.repository.HelpStatisticsRepository;
import com.yukikyu.kook.boot.app.repository.HelpUserLogRepository;
import com.yukikyu.kook.boot.app.service.KookChannelUserService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.StringFilter;

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
    private ChannelService channelService;

    @Autowired
    private ChannelUserService channelUserService;

    @Autowired
    private KookChannelUserService kookChannelUserService;

    @Autowired
    private InviteService inviteService;

    @Autowired
    private GuildService guildService;

    @Autowired
    private KookCommandMatch kookCommandMatch;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private HelpUserLogRepository helpUserLogRepository;

    @Autowired
    private HelpStatisticsRepository helpStatisticsRepository;

    @Autowired
    private HelpStatisticsBusiness helpStatisticsBusiness;

    @EventListener
    public void event(Event event) {
        log.info("事件：{}", event.getClass().getName());
        log.info("source：{}", event.getSource());
        log.info("metadata：{}", event.getMetadata());
    }

    @EventListener
    public void event(KMarkdownEvent event) {
        final var metaData = JSONUtil.parse(event.getMetadata().toString());
        final var author_id = metaData.getByPath("d.extra.author.id", String.class);
        final var guild_id = metaData.getByPath("d.extra.guild_id", String.class);

        // 将服务器信息存入上下文
        LocalContexts.contextsThreadLocal.get().setGuildId(guild_id);

        log.info("\n\n\n{}\n\n\n", metaData);

        final var data = JSONUtil.parse(metaData.getByPath("d", String.class));
        final var type = data.getByPath("channel_type", String.class);

        final var content = data.getByPath("content", String.class);
        final var bot = data.getByPath("extra.author.bot", boolean.class);
        final var chat_channel_id = data.getByPath("target_id", String.class);
        final var msgId = data.getByPath("msg_id", String.class);
        LocalContexts.contextsThreadLocal.get().setChatChannelId(chat_channel_id);
        log.info("消息内容：{}", data);

        // 帮助频道KEY
        String HELP_CHANNEL_ID_KEY = guild_id + "::" + KookBotSettingType.HELP_CHANNEL_ID.name();
        // 组队频道KEY
        String FORM_A_TEAM_CHANNEL_ID_KEY = guild_id + "::" + KookBotSettingType.FORM_A_TEAM_CHANNEL_ID.name();
        // 组队统一通知频道KEY
        String FORM_A_TEAM_UNIFY_NOTIFY_CHANNEL_ID_KEY = guild_id + "::" + KookBotSettingType.FORM_A_TEAM_UNIFY_NOTIFY_CHANNEL_ID.name();
        // 帮助角色IDKEY
        String HELP_ROLE_ID_KEY = guild_id + "::" + KookBotSettingType.HELP_ROLE_ID.name();

        // 回复
        List<MessageRequest> messageRequestList = LocalContexts.contextsThreadLocal.get().getMessageRequestList();

        String guildViewBlock = guildService.getGuildView(Map.of("guild_id", guild_id)).doOnSuccess(log::info).block();

        GuildInfo guildInfo = JSONUtil.toBean(guildViewBlock, GuildInfo.class);

        // 系统设置（服主权限）
        if (ObjectUtil.equals(guildInfo.getData().getUserId(), author_id)) {
            // 设置自定义指令
            if (kookCommandMatch.execute(KookCommandMatchType.SET_CUSTOM_COMMAND, content)) {
                String channel = StrUtil.removeAll(content, LocalContexts.contextsThreadLocal.get().getCommand());
                if (StrUtil.isBlank(channel)) {
                    return;
                }
                channel = channel.trim();
                String[] split = channel.split(" ");
                if (split.length < 2) {
                    return;
                }
                String[] commands = split[1].split(",");

                String key = guild_id + "::COMMAND::" + split[0];
                redisTemplate.delete(key);
                redisTemplate.opsForSet().add(key, commands);
                // 设置成功
                MessageRequest messageRequest = new MessageRequest();
                messageRequest.setContent("设置成功");
                messageRequest.setTempTargetId(author_id);
                messageRequestList.add(messageRequest);
            }
            // 设置帮助角色ID
            if (kookCommandMatch.execute(KookCommandMatchType.SET_HELP_ROLE_ID, content)) {
                String channel = StrUtil.removeAll(content, LocalContexts.contextsThreadLocal.get().getCommand());
                if (StrUtil.isBlank(channel)) {
                    return;
                }
                String[] helpRoleIds = channel.split(",");
                // 角色不存在 TODO
                // reply = "角色不存在";
                redisTemplate.delete(HELP_ROLE_ID_KEY);
                redisTemplate.opsForSet().add(HELP_ROLE_ID_KEY, helpRoleIds);
                // 设置成功
                messageRequestList.add(MessageRequestFactory.success(msgId, author_id));
            }
            // 设置帮助频道
            if (kookCommandMatch.execute(KookCommandMatchType.SET_HELP_CHANNEL_ID, content)) {
                String channel = StrUtil.removeAll(content, LocalContexts.contextsThreadLocal.get().getCommand());
                if (StrUtil.isBlank(channel)) {
                    return;
                }
                stringRedisTemplate.opsForValue().set(HELP_CHANNEL_ID_KEY, channel);
                // 设置成功
                messageRequestList.add(MessageRequestFactory.success(msgId, author_id));
            }
            // 设置组队频道
            else if (kookCommandMatch.execute(KookCommandMatchType.SET_FORM_A_TEAM_CHANNEL_ID, content)) {
                String channel = StrUtil.removeAll(content, LocalContexts.contextsThreadLocal.get().getCommand());
                if (StrUtil.isBlank(channel)) {
                    return;
                }
                stringRedisTemplate.opsForValue().set(FORM_A_TEAM_CHANNEL_ID_KEY, channel);
                // 设置成功
                messageRequestList.add(MessageRequestFactory.success(msgId, author_id));
            }
            // 设置组队统一通知频道
            else if (kookCommandMatch.execute(KookCommandMatchType.SET_FORM_A_TEAM_UNIFY_NOTIFY_CHANNEL_ID, content)) {
                String channel = StrUtil.removeAll(content, LocalContexts.contextsThreadLocal.get().getCommand());
                if (StrUtil.isBlank(channel)) {
                    return;
                }
                redisTemplate.delete(FORM_A_TEAM_UNIFY_NOTIFY_CHANNEL_ID_KEY);
                redisTemplate.opsForSet().add(FORM_A_TEAM_UNIFY_NOTIFY_CHANNEL_ID_KEY, channel.split(","));
                // 设置成功
                messageRequestList.add(MessageRequestFactory.success(msgId, author_id));
            }
            // 删除帮助频道
            else if (kookCommandMatch.execute(KookCommandMatchType.DEL_HELP_CHANNEL_ID, content)) {
                stringRedisTemplate.delete(HELP_CHANNEL_ID_KEY);
                // 设置成功
                messageRequestList.add(MessageRequestFactory.success(msgId, author_id));
            }
            // 删除组队频道
            else if (kookCommandMatch.execute(KookCommandMatchType.DEL_FORM_A_TEAM_CHANNEL_ID, content)) {
                stringRedisTemplate.delete(FORM_A_TEAM_CHANNEL_ID_KEY);
                // 设置成功
                messageRequestList.add(MessageRequestFactory.success(msgId, author_id));
            }
            // 获取帮助统计信息
            else if (kookCommandMatch.execute(KookCommandMatchType.GET_HELP_STATISTICS, content)) {
                // 上月榜
                HelpStatisticsCriteria helpStatisticsCriteria = new HelpStatisticsCriteria();
                LocalDate now = LocalDate.now();
                LocalDate firstDayOfLastMonth = now.minusMonths(1).withDayOfMonth(1); // 获取上个月的第一天
                Instant firstInstant = firstDayOfLastMonth.atStartOfDay(ZoneId.systemDefault()).toInstant(); // 转换为Instant
                InstantFilter monthFilter = new InstantFilter();
                monthFilter.setEquals(firstInstant);
                helpStatisticsCriteria.setMonth(monthFilter);
                StringFilter guildIdFilter = new StringFilter();
                guildIdFilter.setEquals(guild_id);
                helpStatisticsCriteria.setGuildId(guildIdFilter);
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("duration")));
                List<HelpStatistics> helpStatisticsList = helpStatisticsRepository
                    .findByCriteria(helpStatisticsCriteria, pageRequest)
                    .collectList()
                    .block();
                String ct = null;
                if (CollectionUtil.isNotEmpty(helpStatisticsList)) {
                    List<String> helpUserIdList = helpStatisticsList.stream().map(HelpStatistics::getHelpUserId).toList();
                    List<Integer> duration = helpStatisticsList.stream().map(HelpStatistics::getDuration).toList();
                    List<String> hUserIdList = handleRankUser(helpUserIdList);
                    String lastMonthContent = StrUtil.format(
                        KookCommandMatchType.GET_HELP_STATISTICS.getContentTemplate().get("月榜"),
                        firstDayOfLastMonth.getYear(),
                        firstDayOfLastMonth.getMonth().getValue(),
                        "\\n" + String.join("\\n", hUserIdList),
                        "\\n" + String.join("\\n", duration.stream().map(item -> String.valueOf(item / 60 / 30)).toList()),
                        "\\n" + String.join("\\n", duration.stream().map(item -> String.valueOf(item / 60) + "分钟").toList())
                    );
                    ct = lastMonthContent;
                }
                // 总榜
                List<HelpStatistics> allHelpStatisticsList = helpStatisticsRepository
                    .findTotalDurationByGuildIdGroupByHelpUserId(guild_id)
                    .collectList()
                    .block();
                if (CollectionUtil.isNotEmpty(allHelpStatisticsList)) {
                    List<String> helpUserIdList = allHelpStatisticsList.stream().map(HelpStatistics::getHelpUserId).toList();
                    List<Integer> duration = allHelpStatisticsList.stream().map(HelpStatistics::getDuration).toList();
                    List<String> hUserIdList = handleRankUser(helpUserIdList);
                    String allContent = StrUtil.format(
                        KookCommandMatchType.GET_HELP_STATISTICS.getContentTemplate().get("总榜"),
                        "\\n" + String.join("\\n", hUserIdList),
                        "\\n" + String.join("\\n", duration.stream().map(item -> String.valueOf(item / 60 / 30)).toList()),
                        "\\n" + String.join("\\n", duration.stream().map(item -> String.valueOf(item / 60) + "分钟").toList())
                    );

                    if (ct != null) {
                        ct += "," + allContent;
                    }
                }
                if (ct != null) {
                    MessageRequest messageRequest = new MessageRequest();
                    messageRequest.setType(KookMessageType.CARD.getValue());
                    messageRequest.setTargetId(chat_channel_id);
                    messageRequest.setContent("[" + ct + "]");
                    messageRequestList.add(messageRequest);
                }
            }
            // TODO 获取帮助信息
            else if (kookCommandMatch.execute(KookCommandMatchType.GET_HELP, content)) {}
            // 手动刷新统计信息
            else if (kookCommandMatch.execute(KookCommandMatchType.REFRESH_HELP_STATISTICS, content)) {
                helpStatisticsBusiness.refresh(LocalDate.now());
            }
            // 手动刷新上月统计信息
            else if (kookCommandMatch.execute(KookCommandMatchType.REFRESH_HELP_STATISTICS, content)) {
                helpStatisticsBusiness.refresh(LocalDate.now().minusMonths(1));
            }
        }

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

        //对方是否为机器人
        if (bot) {
            return;
        }

        if (type.equals("PERSON")) {} else if (type.equals("GROUP")) {
            // {"code":0,"message":"操作成功","data":{"items":[{"id":"1635770006169182","guild_id":"7363287837020870","master_id":"2431192162","parent_id":"8097939466864975","user_id":"2431192162","name":"闲聊","topic":"","type":2,"level":200,"slow_mode":0,"last_msg_content":"","last_msg_id":"","has_password":false,"limit_amount":25,"is_category":false,"permission_sync":1,"permission_overwrites":[{"role_id":0,"allow":0,"deny":0}],"permission_users":[]}],"meta":{"page":1,"page_total":1,"page_size":50,"total":1},"sort":{}}}
            List<Channel> joinedChannel = kookChannelUserService.getJoinedChannel(guild_id, author_id);
            // 组队指令
            if (kookCommandMatch.execute(KookCommandMatchType.FORM_A_TEAM, content)) {
                // 未加入语音频道
                if (ObjectUtil.isEmpty(joinedChannel)) {
                    String messageContent = StrUtil.format(" (met){}(met) 未加入语音频道，该组队未生效，请加入语音频道后重试。", author_id);
                    MessageRequest messageRequest = new MessageRequest();
                    messageRequest.setType(KookMessageType.KMARKDOWN.getValue());
                    messageRequest.setTargetId(chat_channel_id);
                    messageRequest.setQuote(msgId);
                    messageRequest.setContent(messageContent);
                    messageRequestList.add(messageRequest);
                    log.info("未加入语音频道");
                    return;
                }
                final String channel_id;
                if (joinedChannel != null) {
                    channel_id = joinedChannel.get(0).getId();
                } else {
                    String messageContent = StrUtil.format(" (met){}(met) 未加入语音频道，该组队未生效，请加入语音频道后重试。", author_id);
                    MessageRequest messageRequest = new MessageRequest();
                    messageRequest.setType(KookMessageType.KMARKDOWN.getValue());
                    messageRequest.setTargetId(chat_channel_id);
                    messageRequest.setQuote(msgId);
                    messageRequest.setContent(messageContent);
                    messageRequestList.add(messageRequest);
                    log.info("未加入语音频道");
                    return;
                }

                // 指定组队频道
                String formATeamChannelId = stringRedisTemplate.opsForValue().get(FORM_A_TEAM_CHANNEL_ID_KEY);
                if (StrUtil.isNotEmpty(formATeamChannelId) && ObjectUtil.notEqual(formATeamChannelId, chat_channel_id)) {
                    String messageContent = StrUtil.format(
                        " (met){}(met) 该频道不是指定组队频道，请移步至 (chn){}(chn) 频道发送组队信息。",
                        author_id,
                        formATeamChannelId
                    );
                    MessageRequest messageRequest = new MessageRequest();
                    messageRequest.setType(KookMessageType.KMARKDOWN.getValue());
                    messageRequest.setTargetId(chat_channel_id);
                    messageRequest.setQuote(msgId);
                    messageRequest.setContent(messageContent);
                    messageRequestList.add(messageRequest);
                    log.info("不是指定组队频道");
                    return;
                }

                // {"code":0,"message":"操作成功","data":{"url":"https://kook.top/G9ivRG","url_code":"G9ivRG"}}
                Invite invite = createInvite(channel_id);
                if (invite == null) {
                    String messageContent = StrUtil.format(" (met){}(met) 邀请创建失败，请重试。", author_id);
                    MessageRequest messageRequest = new MessageRequest();
                    messageRequest.setType(KookMessageType.KMARKDOWN.getValue());
                    messageRequest.setTargetId(chat_channel_id);
                    messageRequest.setQuote(msgId);
                    messageRequest.setContent(messageContent);
                    messageRequestList.add(messageRequest);
                    log.info("邀请创建失败，请重试");
                    return;
                }
                // {"s":0,"d":{"channel_type":"GROUP","type":9,"target_id":"3163216254858192","author_id":"2431192162","content":"333","extra":{"type":9,"code":"","guild_id":"7363287837020870","channel_name":"\u6d4b\u8bd5","author":{"id":"2431192162","username":"\u96ea\u7403\u4e36","identify_num":"9841","online":true,"os":"Websocket","status":1,"avatar":"https:\/\/img.kookapp.cn\/avatars\/2022-05\/4cdLRdZGbB07y07y.jpg?x-oss-process=style\/icon","vip_avatar":"https:\/\/img.kookapp.cn\/avatars\/2022-05\/4cdLRdZGbB07y07y.jpg?x-oss-process=style\/icon","banner.txt":"","nickname":"\u96ea\u7403\u4e36","roles":[],"is_vip":false,"is_ai_reduce_noise":true,"is_personal_card_bg":false,"bot":false,"decorations_id_map":null,"is_sys":false},"visible_only":null,"mention":[],"mention_all":false,"mention_roles":[],"mention_here":false,"nav_channels":[],"kmarkdown":{"raw_content":"333","mention_part":[],"mention_role_part":[],"channel_part":[]},"emoji":[],"last_msg_content":"\u96ea\u7403\u4e36\uff1a333","send_msg_device":1},"msg_id":"c285800b-e782-42b0-89cb-854208130b6f","msg_timestamp":1687969072819,"nonce":"2dJSaKesSdC1VtwSICzNvTi1","from_type":1},"extra":{"verifyToken":"4BnfeTACRFDr_mKQ","encryptKey":"Pq2sDV7oq","callbackUrl":""},"sn":1}

                // 通知频道
                Set<Object> members = redisTemplate.opsForSet().members(FORM_A_TEAM_UNIFY_NOTIFY_CHANNEL_ID_KEY);

                if (CollectionUtil.isNotEmpty(members)) {
                    List<MessageRequest> newMessageRequestList = members
                        .stream()
                        .map(member -> {
                            MessageRequest messageRequest = new MessageRequest();
                            messageRequest.setType(KookMessageType.CARD.getValue());
                            messageRequest.setTargetId(String.valueOf(member));
                            messageRequest.setContent(
                                StrUtil.format(
                                    KookCommandMatchType.FORM_A_TEAM.getContentTemplate().get("DEFAULT"),
                                    author_id,
                                    content,
                                    invite.getUrlCode(),
                                    formATeamChannelId
                                )
                            );
                            return messageRequest;
                        })
                        .toList();
                    messageRequestList.addAll(newMessageRequestList);
                }
                MessageRequest messageRequest = new MessageRequest();
                messageRequest.setType(KookMessageType.CARD.getValue());
                messageRequest.setTargetId(chat_channel_id);
                messageRequest.setContent(
                    StrUtil.format(
                        KookCommandMatchType.FORM_A_TEAM.getContentTemplate().get("DEFAULT"),
                        author_id,
                        content,
                        invite.getUrlCode(),
                        formATeamChannelId
                    )
                );
                messageRequest.setQuote(msgId);
                messageRequestList.add(messageRequest);
            }
            // 帮助指令
            else if (kookCommandMatch.execute(KookCommandMatchType.HELP, content)) {
                if (ObjectUtil.isEmpty(joinedChannel)) {
                    String messageContent = StrUtil.format(" (met){}(met) 未加入语音频道，该求助未生效，请加入语音频道后重试。", author_id);
                    MessageRequest messageRequest = new MessageRequest();
                    messageRequest.setType(KookMessageType.KMARKDOWN.getValue());
                    messageRequest.setTargetId(chat_channel_id);
                    messageRequest.setQuote(msgId);
                    messageRequest.setContent(messageContent);
                    messageRequestList.add(messageRequest);
                    log.info("未加入语音频道");
                    return;
                }
                // 指定帮助频道
                String helpChannelId = stringRedisTemplate.opsForValue().get(HELP_CHANNEL_ID_KEY);
                if (StrUtil.isNotEmpty(helpChannelId) && ObjectUtil.notEqual(helpChannelId, chat_channel_id)) {
                    String messageContent = StrUtil.format(
                        " (met){}(met) 该频道不是指定求助频道，请移步至 (chn){}(chn) 频道发送求助信息。",
                        author_id,
                        helpChannelId
                    );
                    MessageRequest messageRequest = new MessageRequest();
                    messageRequest.setType(KookMessageType.KMARKDOWN.getValue());
                    messageRequest.setTargetId(chat_channel_id);
                    messageRequest.setQuote(msgId);
                    messageRequest.setContent(messageContent);
                    messageRequestList.add(messageRequest);
                    log.info("不是指定帮助频道");
                    return;
                }

                // 必须在语音频道
                if (joinedChannel == null) {
                    String messageContent = StrUtil.format(" (met){}(met) 未加入语音频道，该求助未生效，请加入语音频道后重试。", author_id);
                    MessageRequest messageRequest = new MessageRequest();
                    messageRequest.setType(KookMessageType.KMARKDOWN.getValue());
                    messageRequest.setTargetId(chat_channel_id);
                    messageRequest.setQuote(msgId);
                    messageRequest.setContent(messageContent);
                    messageRequestList.add(messageRequest);
                    log.info("未加入语音频道");
                    return;
                }

                MessageRequest messageRequest = new MessageRequest();
                messageRequest.setType(KookMessageType.CARD.getValue());
                messageRequest.setTargetId(chat_channel_id);
                String btnValue = guild_id + "::BTN::" + IdUtil.getSnowflake().nextIdStr();
                messageRequest.setContent(
                    StrUtil.format(
                        KookCommandMatchType.HELP.getContentTemplate().get("ASK"),
                        btnValue,
                        stringRedisTemplate.opsForValue().get(HELP_CHANNEL_ID_KEY)
                    )
                );
                messageRequest.setQuote(msgId);
                messageRequestList.add(messageRequest);

                HelpBtnParam helpBtnParam = new HelpBtnParam();
                helpBtnParam.setAuthorId(author_id);
                helpBtnParam.setType(KookCommandMatchType.HELP.name());
                helpBtnParam.setGuildId(guild_id);
                stringRedisTemplate.opsForValue().set(btnValue, JSONUtil.parse(helpBtnParam).toJSONString(0));
            }
            // 个人积分查询
            else if (kookCommandMatch.execute(KookCommandMatchType.GET_PERSON_POINT, content)) {
                // 数值
                String currentMonthPoint = "0";
                String currentMonthTime = "0";
                String lastMonthPoint = "0";
                String lastMonthTime = "0";
                String totalMonthPoint = "0";
                String totalMonthTime = "0";

                // 消息内容
                String messageContent;

                try {
                    // 查询个人积分
                    HelpStatisticsCriteria helpStatisticsCriteria = new HelpStatisticsCriteria();
                    // 帮助人
                    StringFilter helpUserIdFilter = new StringFilter();
                    helpUserIdFilter.setEquals(author_id);
                    helpStatisticsCriteria.setHelpUserId(helpUserIdFilter);
                    // 服务器
                    StringFilter guildIdFilter = new StringFilter();
                    guildIdFilter.setEquals(guild_id);
                    helpStatisticsCriteria.setGuildId(guildIdFilter);

                    // - 总
                    List<HelpStatistics> totalMonthHelpStatisticsList = helpStatisticsRepository
                        .findByCriteria(helpStatisticsCriteria, null)
                        .collectList()
                        .block();
                    if (CollectionUtil.isNotEmpty(totalMonthHelpStatisticsList)) {
                        int sumDuration = 0;
                        for (int i = 0; i < totalMonthHelpStatisticsList.size(); i++) {
                            HelpStatistics helpStatistics = totalMonthHelpStatisticsList.get(i);
                            sumDuration += helpStatistics.getDuration();
                        }
                        totalMonthPoint = String.valueOf(sumDuration / 60 / 30);
                        totalMonthTime = String.valueOf(sumDuration / 60);
                    }

                    // 月份(本月)
                    InstantFilter monthFilter = new InstantFilter();
                    LocalDate localDate = LocalDate.now();
                    Instant firstInstant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant(); // 转换为Instant
                    monthFilter.setEquals(firstInstant);
                    helpStatisticsCriteria.setMonth(monthFilter);

                    // - 本月
                    List<HelpStatistics> currentMonthHelpStatisticsList = helpStatisticsRepository
                        .findByCriteria(helpStatisticsCriteria, null)
                        .collectList()
                        .block();
                    if (CollectionUtil.isNotEmpty(currentMonthHelpStatisticsList)) {
                        int sumDuration = 0;
                        for (int i = 0; i < currentMonthHelpStatisticsList.size(); i++) {
                            HelpStatistics helpStatistics = currentMonthHelpStatisticsList.get(i);
                            sumDuration += helpStatistics.getDuration();
                        }
                        currentMonthPoint = String.valueOf(sumDuration / 60 / 30);
                        currentMonthTime = String.valueOf(sumDuration / 60);
                    }

                    // 月份(上个月)
                    monthFilter = new InstantFilter();
                    localDate = LocalDate.now().minusMonths(1);
                    LocalDate firstDayOfLastMonth = localDate.withDayOfMonth(1); // 获取上个月的第一天
                    firstInstant = firstDayOfLastMonth.atStartOfDay(ZoneId.systemDefault()).toInstant(); // 转换为Instant
                    monthFilter.setEquals(firstInstant);
                    helpStatisticsCriteria.setMonth(monthFilter);

                    // - 上月
                    List<HelpStatistics> lastMonthHelpStatisticsList = helpStatisticsRepository
                        .findByCriteria(helpStatisticsCriteria, null)
                        .collectList()
                        .block();
                    if (CollectionUtil.isNotEmpty(lastMonthHelpStatisticsList)) {
                        int sumDuration = 0;
                        for (int i = 0; i < lastMonthHelpStatisticsList.size(); i++) {
                            HelpStatistics helpStatistics = lastMonthHelpStatisticsList.get(i);
                            sumDuration += helpStatistics.getDuration();
                        }
                        lastMonthPoint = String.valueOf(sumDuration / 60 / 30);
                        lastMonthTime = String.valueOf(sumDuration / 60);
                    }
                    messageContent =
                        StrUtil.format(
                            KookCommandMatchType.GET_PERSON_POINT.getContentTemplate().get("个人积分详情"),
                            author_id,
                            currentMonthPoint,
                            currentMonthTime,
                            lastMonthPoint,
                            lastMonthTime,
                            totalMonthPoint,
                            totalMonthTime
                        );
                } catch (Exception e) {
                    messageContent = "系统错误，请联系服务器管理员。";
                    log.error("个人积分查询错误：{}", e.getMessage());
                }

                MessageRequest messageRequest = new MessageRequest();
                messageRequest.setType(KookMessageType.CARD.getValue());
                messageRequest.setTargetId(chat_channel_id);
                messageRequest.setQuote(msgId);
                messageRequest.setContent(messageContent);
                messageRequestList.add(messageRequest);
            }
        }
    }

    @EventListener
    public void event(MessageBtnClick event) {
        // {"s":0,"d":{"channel_type":"PERSON","type":255,"target_id":"3950503322","author_id":"1","content":"[\u7cfb\u7edf\u6d88\u606f]","extra":{"type":"message_btn_click","body":{"value":"2222","msg_id":"41622df0-7de7-4230-80e8-afad71aeea1c","user_id":"2431192162","target_id":"7837798215125728","channel_type":"GROUP","user_info":{"id":"2431192162","username":"\u96ea\u7403\u4e36","identify_num":"9841","online":true,"os":"Websocket","status":1,"avatar":"https:\/\/img.kookapp.cn\/avatars\/2022-05\/4cdLRdZGbB07y07y.jpg?x-oss-process=style\/icon","vip_avatar":"https:\/\/img.kookapp.cn\/avatars\/2022-05\/4cdLRdZGbB07y07y.jpg?x-oss-process=style\/icon","banner":"","nickname":"\u96ea\u7403\u4e36","roles":[],"is_vip":false,"vip_amp":false,"is_ai_reduce_noise":true,"is_personal_card_bg":false,"bot":false,"decorations_id_map":{"background":10208},"mobile_verified":true,"is_sys":false,"joined_at":1686034561000,"active_time":1688893722249},"guild_id":"7363287837020870"}},"msg_id":"3899d3a0-a625-4100-b234-309453b8e0a6","msg_timestamp":1688893724725,"nonce":"","from_type":1},"extra":{"verifyToken":"4BnfeTACRFDr_mKQ","encryptKey":"Pq2sDV7oq","callbackUrl":""},"sn":4}
        log.info("metadata:{}", event.getMetadata());
        final var metaData = JSONUtil.parse(event.getMetadata().toString());
        final var msg_id = metaData.getByPath("d.extra.body.msg_id", String.class);
        final var value = metaData.getByPath("d.extra.body.value", String.class);
        final var guild_id = metaData.getByPath("d.extra.body.guild_id", String.class);
        final var user_id = metaData.getByPath("d.extra.body.user_id", String.class);
        String valueString = stringRedisTemplate.opsForValue().get(value);
        BaseBtnParamEntity abstractBtnParamEntity = JSONUtil.toBean(valueString, BaseBtnParamEntity.class);
        // 求助信息
        if (ObjectUtil.equals(KookCommandMatchType.HELP.name(), abstractBtnParamEntity.getType())) {
            // 求助按钮必须作者点
            if (ObjectUtil.notEqual(abstractBtnParamEntity.getAuthorId(), user_id)) {
                return;
            }
            Set<Object> helpRoleIdObjectList = redisTemplate.opsForSet().members(guild_id + "::" + KookBotSettingType.HELP_ROLE_ID.name());
            if (CollectionUtil.isEmpty(helpRoleIdObjectList)) {
                log.info("未设置帮助角色");
                return;
            }

            List<String> helpRoleIdList = helpRoleIdObjectList.stream().map(String::valueOf).toList();

            List<Channel> joinedChannel = kookChannelUserService.getJoinedChannel(guild_id, user_id);
            // 必须拥有语音频道
            if (CollectionUtil.isEmpty(joinedChannel)) {
                return;
            }

            // 发送求助信息
            Set<String> helpUserIdSet = new HashSet<>();

            for (int i = 0; i < helpRoleIdList.size(); i++) {
                // {"code":0,"message":"操作成功","data":{"items":[{"id":"2431192162","username":"雪球丶","nickname":"雪球丶","identify_num":"9841","online":true,"bot":false,"status":1,"banner":"","avatar":"https://img.kookapp.cn/avatars/2022-05/4cdLRdZGbB07y07y.jpg?x-oss-process=style/icon","vip_avatar":"https://img.kookapp.cn/avatars/2022-05/4cdLRdZGbB07y07y.jpg?x-oss-process=style/icon","mobile_verified":true,"joined_at":1686034561000,"active_time":1688900277553,"roles":[23077198],"is_master":true,"abbr":"","color":3447003}],"meta":{"page":1,"page_total":1,"page_size":50,"total":1},"sort":{},"user_count":27,"online_count":1,"offline_count":0}}
                String guildUserListResponse = guildService
                    .getGuildUserList(Map.of("guild_id", guild_id, "role_id", helpRoleIdList.get(i)))
                    .doOnSuccess(System.out::println)
                    .block();
                final var guildUserListResponseJson = JSONUtil.parse(guildUserListResponse);
                JSONArray guildUserListJsonArray = JSONUtil.parseArray(guildUserListResponseJson.getByPath("data.items", String.class));
                List<UserInfo> userInfoList = JSONUtil.toList(guildUserListJsonArray, UserInfo.class);
                if (CollectionUtil.isNotEmpty(userInfoList)) {
                    helpUserIdSet.addAll(userInfoList.stream().map(UserInfo::getId).collect(Collectors.toSet()));
                }
            }

            // 获取邀请链接
            String channelId = joinedChannel.get(0).getId();
            Invite invite = createInvite(channelId, 1);
            if (invite == null) {
                return;
            }

            // 添加标记
            redisTemplate.opsForValue().set(KookCommandMatchType.HELP.name() + "::" + channelId, 0);

            // 私信发送求助信息
            List<String> msg_id_list = helpUserIdSet
                .stream()
                .map(helpUserId -> {
                    // {"code":0,"message":"操作成功","data":{"code":"b22ff8a1361f4993804d0dc6dbffbfe1","msg_id":"2d0d0182-a428-4902-979b-43a066209c71","msg_timestamp":1688924860376,"nonce":""}}
                    String postDirectMessageCreateResponse = directMessageService
                        .postDirectMessageCreate(
                            Map.of(
                                "type",
                                KookMessageType.CARD.getValue(),
                                "target_id",
                                helpUserId,
                                "content",
                                StrUtil.format(
                                    KookCommandMatchType.HELP.getContentTemplate().get("HELP_MESSAGE"),
                                    user_id,
                                    invite.getUrlCode()
                                )
                            )
                        )
                        .doOnSuccess(log::info)
                        .block();
                    return JSONUtil.parse(postDirectMessageCreateResponse).getByPath("data.msg_id", String.class);
                })
                .toList();
            // 修改信息
            String HELP_CHANNEL_ID_KEY = guild_id + "::" + KookBotSettingType.HELP_CHANNEL_ID.name();
            Map<String, String> stringMap = Map.of(
                "msg_id",
                msg_id,
                "content",
                StrUtil.format(
                    KookCommandMatchType.HELP.getContentTemplate().get("WAITING"),
                    stringRedisTemplate.opsForValue().get(HELP_CHANNEL_ID_KEY)
                )
            );
            String block = messageService.postMessageUpdate(stringMap).doOnSuccess(log::info).block();
            log.info("修改信息：{}", block);
            redisTemplate.opsForValue().set(KookCommandMatchType.HELP.name() + "::" + channelId, user_id);
            stringRedisTemplate.opsForValue().set(KookCommandMatchType.HELP.name() + "::" + channelId + "::FLAG", msg_id);
            String key = KookCommandMatchType.HELP.name() + "::" + channelId + "::MESSAGE_ID_LIST";
            redisTemplate.delete(key);
            redisTemplate.opsForSet().add(key, ArrayUtil.toArray(msg_id_list, String.class));
        }
    }

    @EventListener
    public void event(JoinedChannel event) {
        // {"s":0,"d":{"channel_type":"GROUP","type":255,"target_id":"7363287837020870","author_id":"1","content":"[\u7cfb\u7edf\u6d88\u606f]","extra":{"type":"joined_channel","body":{"user_id":"2431192162","channel_id":"5362986112653973","joined_at":1688916950984}},"msg_id":"4cdea6bb-cf8f-47d3-ad24-56ba88b23947","msg_timestamp":1688916951031,"nonce":"","from_type":1},"extra":{"verifyToken":"4BnfeTACRFDr_mKQ","encryptKey":"Pq2sDV7oq","callbackUrl":""},"sn":2}
        JSON json = JSONUtil.parse(event.getMetadata());
        String channel_id = json.getByPath("d.extra.body.channel_id", String.class);
        String key = KookCommandMatchType.HELP.name() + "::" + channel_id + "::FLAG";
        String user_id = json.getByPath("d.extra.body.user_id", String.class);
        String help_channel_flag = stringRedisTemplate.opsForValue().get(key);
        // 帮助用户加入
        if (ObjectUtil.isNotNull(help_channel_flag)) {
            // String userId = stringRedisTemplate.opsForValue().get(KookCommandMatchType.HELP.name() + "::" + channel_id);
            // 查询该频道人员
            String getChannelUserListResponse = channelService.getChannelUserList(Map.of("channel_id", channel_id)).block();
            JSON getChannelUserListResponseJson = JSONUtil.parse(getChannelUserListResponse);
            JSONArray jsonArray = getChannelUserListResponseJson.getByPath("data", JSONArray.class);
            List<UserInfo> userInfoList = JSONUtil.toList(jsonArray, UserInfo.class);
            List<UserInfo> otherUserInfoList = userInfoList.stream().filter(item -> ObjectUtil.notEqual(user_id, item.getId())).toList();
            // 修改帮助信息
            Map<String, String> stringMap = Map.of(
                "msg_id",
                help_channel_flag,
                "content",
                KookCommandMatchType.HELP.getContentTemplate().get("SUCCESS")
            );
            messageService.postMessageUpdate(stringMap).doOnSuccess(log::info).doOnError(Throwable::printStackTrace).block();
            // 存在其他用户
            if (CollectionUtil.isNotEmpty(otherUserInfoList)) {
                String guild_id = json.getByPath("d.target_id", String.class);
                List<HelpUserLog> helpUserLogList = otherUserInfoList
                    .stream()
                    .map(UserInfo::getId)
                    .collect(Collectors.toSet())
                    .stream()
                    .map(item -> {
                        // 统计帮助时间
                        HelpUserLog helpUserLog = new HelpUserLog();
                        helpUserLog.setGuildId(guild_id);
                        helpUserLog.channelId(channel_id);
                        helpUserLog.setUserId(item);
                        helpUserLog.setHelpUserId(user_id);
                        helpUserLog.setJoinAt(Instant.now());
                        helpUserLog.setExitAt(Instant.now());
                        helpUserLog.setStatus(HelpLogStatus.STARTING.name());
                        return helpUserLog;
                    })
                    .toList();

                helpUserLogRepository.saveAll(helpUserLogList).subscribe();

                stringRedisTemplate.delete(key);

                // 修改帮助信息
                // redisTemplate.opsForValue().set(KookCommandMatchType.HELP.name() + "::" + channelId + "::MESSAGE_ID_LIST", msg_id_list);
                Set<Object> messageIdList = redisTemplate
                    .opsForSet()
                    .members(KookCommandMatchType.HELP.name() + "::" + channel_id + "::MESSAGE_ID_LIST");
                messageIdList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(msgId -> {
                        directMessageService
                            .postDirectMessageUpdate(
                                Map.of("msg_id", msgId, "content", KookCommandMatchType.HELP.getContentTemplate().get("HELP_MESSAGE"))
                            )
                            .doOnSuccess(log::info)
                            .block();
                    });
            }
        }

        List<HelpUserLog> firstHelpUserLog = helpUserLogRepository
            .findByChannelIdAndStatus(channel_id, HelpLogStatus.STARTING.name())
            .collectList()
            .block();

        // 不是帮助人
        if (CollectionUtil.isNotEmpty(firstHelpUserLog) && ObjectUtil.notEqual(user_id, firstHelpUserLog.get(0).getHelpUserId())) {
            HelpUserLog huLog = firstHelpUserLog.get(0);
            HelpUserLog helpUserLog = new HelpUserLog();
            helpUserLog.setChannelId(huLog.getChannelId());
            helpUserLog.setGuildId(huLog.getGuildId());
            helpUserLog.setUserId(user_id);
            helpUserLog.setHelpUserId(huLog.getHelpUserId());
            helpUserLog.setJoinAt(Instant.now());
            helpUserLog.setExitAt(Instant.now());
            helpUserLog.setStatus(HelpLogStatus.STARTING.name());
            helpUserLogRepository.save(helpUserLog).doOnSuccess(item -> log.info("保存后加入的萌新：{}", item)).blockOptional();
        }
    }

    @EventListener
    public void event(ExitedChannel event) {
        // {"s":0,"d":{"channel_type":"GROUP","type":255,"target_id":"7363287837020870","author_id":"1","content":"[\u7cfb\u7edf\u6d88\u606f]","extra":{"type":"joined_channel","body":{"user_id":"2431192162","channel_id":"5362986112653973","joined_at":1688916950984}},"msg_id":"4cdea6bb-cf8f-47d3-ad24-56ba88b23947","msg_timestamp":1688916951031,"nonce":"","from_type":1},"extra":{"verifyToken":"4BnfeTACRFDr_mKQ","encryptKey":"Pq2sDV7oq","callbackUrl":""},"sn":2}
        JSON json = JSONUtil.parse(event.getMetadata());
        String channel_id = json.getByPath("d.extra.body.channel_id", String.class);
        String user_id = json.getByPath("d.extra.body.user_id", String.class);
        String key = KookCommandMatchType.HELP.name() + "::" + channel_id;
        List<HelpUserLog> helpUserLogList = helpUserLogRepository
            .findByChannelIdAndStatus(channel_id, HelpLogStatus.STARTING.name())
            .collectList()
            .block();

        // 如果存在开始中的帮助LOG
        if (CollectionUtil.isNotEmpty(helpUserLogList)) {
            // 是否是帮助人
            Optional<HelpUserLog> helpUserOptionalHelpUserLog = helpUserLogList
                .stream()
                .filter(item -> ObjectUtil.equals(item.getHelpUserId(), user_id))
                .toList()
                .stream()
                .findFirst();
            // 如果是帮助人
            if (helpUserOptionalHelpUserLog.isPresent()) {
                // 记录帮助退出
                HelpUserLog helpUserhelpUserLog = helpUserOptionalHelpUserLog.get();
                String helpUserId = helpUserhelpUserLog.getHelpUserId();
                if (helpUserId == null) {
                    return;
                }

                helpUserLogRepository.updateStatusAndExitAtByHelpUserId(HelpLogStatus.ENDED.name(), Instant.now(), helpUserId).block();

                // 删除标记
                redisTemplate.delete(key + "*");

                return;
            }

            // 是否是求助人
            Optional<HelpUserLog> userOptionalHelpUserLog = helpUserLogList
                .stream()
                .filter(item -> ObjectUtil.equals(item.getUserId(), user_id))
                .toList()
                .stream()
                .findFirst();
            if (userOptionalHelpUserLog.isPresent()) {
                HelpUserLog helpUserLog = userOptionalHelpUserLog.get();
                helpUserLog.setExitAt(Instant.now());
                helpUserLog.setStatus(HelpLogStatus.ENDED.name());
                helpUserLogRepository.save(helpUserLog).block();
                // 删除标记
                redisTemplate.delete(key + "*");
            }

            Optional<List<HelpUserLog>> laterOptionalHelpUserLogList = helpUserLogRepository
                .findByChannelIdAndStatus(channel_id, HelpLogStatus.STARTING.name())
                .collectList()
                .blockOptional();
            // 如果不存在正在进行求助LOG
            if (laterOptionalHelpUserLogList.isEmpty()) {
                redisTemplate.delete(key + "*");
            }
        }
    }

    /**
     * 处理排名用户
     *
     * @param helpUserIdList
     * @return
     */
    private static List<String> handleRankUser(List<String> helpUserIdList) {
        AtomicInteger flag = new AtomicInteger();
        return helpUserIdList
            .stream()
            .map(helpUserId -> {
                String met = "(met)" + helpUserId + "(met)";
                if (flag.get() <= 2) {
                    String emoji = Constants.EMOJIS[flag.get()];
                    flag.getAndAdd(1);
                    return StrUtil.addPrefixIfNot(met, emoji);
                } else {
                    String s = StrUtil.addPrefixIfNot(met, flag.get() + 1 + "\u20E3");
                    flag.getAndAdd(1);
                    return s;
                }
            })
            .collect(Collectors.toList());
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
