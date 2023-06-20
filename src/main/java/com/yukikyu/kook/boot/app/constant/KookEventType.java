package com.yukikyu.kook.boot.app.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * kook事件类型
 *
 * @author: yukikyu
 * @date: 2023-06-20 14:40
 */
@AllArgsConstructor
@Getter
public enum KookEventType {
    GUILD_MEMBER_ONLINE("公会成员在线", "GuildMemberOnline", "cn.enaium.kookstarter.event.GuildMemberOnline"),
    DELETED_CHANNEL("已删除频道", "DeletedChannel", "cn.enaium.kookstarter.event.DeletedChannel"),
    DELETED_ROLE("已删除角色", "DeletedRole", "cn.enaium.kookstarter.event.DeletedRole"),
    IMAGE_ANIMATION_EVENT("图像动画事件", "ImageAnimationEvent", "cn.enaium.kookstarter.event.ImageAnimationEvent"),
    ADDED_ROLE("添加角色", "AddedRole", "cn.enaium.kookstarter.event.AddedRole"),
    UPDATED_GUILD_MEMBER("更新公会成员", "UpdatedGuildMember", "cn.enaium.kookstarter.event.UpdatedGuildMember"),
    DELETED_REACTION("删除反应", "DeletedReaction", "cn.enaium.kookstarter.event.DeletedReaction"),
    UPDATED_PRIVATE_MESSAGE("更新私人消息", "UpdatedPrivateMessage", "cn.enaium.kookstarter.event.UpdatedPrivateMessage"),
    ADDED_EMOJI("添加表情符号", "AddedEmoji", "cn.enaium.kookstarter.event.AddedEmoji"),
    ADDED_REACTION("添加反应", "AddedReaction", "cn.enaium.kookstarter.event.AddedReaction"),
    PRIVATE_ADDED_REACTION("私人添加反应", "PrivateAddedReaction", "cn.enaium.kookstarter.event.PrivateAddedReaction"),
    DELETED_GUILD("删除公会", "DeletedGuild", "cn.enaium.kookstarter.event.DeletedGuild"),
    USER_UPDATED("用户更新", "UserUpdated", "cn.enaium.kookstarter.event.UserUpdated"),
    REMOVED_EMOJI("删除表情符号", "RemovedEmoji", "cn.enaium.kookstarter.event.RemovedEmoji"),
    DELETED_MESSAGE("删除消息", "DeletedMessage", "cn.enaium.kookstarter.event.DeletedMessage"),
    K_MARKDOWN_EVENT("KMarkdown事件", "KMarkdownEvent", "cn.enaium.kookstarter.event.KMarkdownEvent"),
    AUDIO_EVENT("音频事件", "AudioEvent", "cn.enaium.kookstarter.event.AudioEvent"),
    IMAGE_EVENT("图像事件", "ImageEvent", "cn.enaium.kookstarter.event.ImageEvent"),
    SELF_JOINED_GUILD("自己加入公会", "SelfJoinedGuild", "cn.enaium.kookstarter.event.SelfJoinedGuild"),
    FILE_EVENT("文件事件", "FileEvent", "cn.enaium.kookstarter.event.FileEvent"),
    UNPINNED_MESSAGE("取消置顶消息", "UnpinnedMessage", "cn.enaium.kookstarter.event.UnpinnedMessage"),
    PINNED_MESSAGE("置顶消息", "PinnedMessage", "cn.enaium.kookstarter.event.PinnedMessage"),
    MESSAGE_BTN_CLICK("消息按钮点击", "MessageBtnClick", "cn.enaium.kookstarter.event.MessageBtnClick"),
    ADDED_CHANNEL("添加频道", "AddedChannel", "cn.enaium.kookstarter.event.AddedChannel"),
    UPDATED_CHANNEL("更新频道", "UpdatedChannel", "cn.enaium.kookstarter.event.UpdatedChannel"),
    PRIVATE_DELETED_REACTION("私人删除反应", "PrivateDeletedReaction", "cn.enaium.kookstarter.event.PrivateDeletedReaction"),
    DELETED_PRIVATE_MESSAGE("删除私人消息", "DeletedPrivateMessage", "cn.enaium.kookstarter.event.DeletedPrivateMessage"),
    JOINED_GUILD("加入公会", "JoinedGuild", "cn.enaium.kookstarter.event.JoinedGuild"),
    VIDEO_EVENT("视频事件", "VideoEvent", "cn.enaium.kookstarter.event.VideoEvent"),
    SELF_EXITED_GUILD("自己退出公会", "SelfExitedGuild", "cn.enaium.kookstarter.event.SelfExitedGuild"),
    ADDED_BLOCK_LIST("添加屏蔽列表", "AddedBlockList", "cn.enaium.kookstarter.event.AddedBlockList"),
    DELETED_BLOCK_LIST("删除屏蔽列表", "DeletedBlockList", "cn.enaium.kookstarter.event.DeletedBlockList"),
    UPDATED_ROLE("更新角色", "UpdatedRole", "cn.enaium.kookstarter.event.UpdatedRole"),
    UPDATED_GUILD("更新公会", "UpdatedGuild", "cn.enaium.kookstarter.event.UpdatedGuild"),
    TEXT_EVENT("文本事件", "TextEvent", "cn.enaium.kookstarter.event.TextEvent"),
    CARD_EVENT("卡片事件", "CardEvent", "cn.enaium.kookstarter.event.CardEvent"),
    EXITED_CHANNEL("退出频道", "ExitedChannel", "cn.enaium.kookstarter.event.ExitedChannel"),
    UPDATED_MESSAGE("更新消息", "UpdatedMessage", "cn.enaium.kookstarter.event.UpdatedMessage"),
    JOINED_CHANNEL("加入频道", "JoinedChannel", "cn.enaium.kookstarter.event.JoinedChannel"),
    EXITED_GUILD("退出公会", "ExitedGuild", "cn.enaium.kookstarter.event.ExitedGuild"),
    UPDATED_EMOJI("更新表情符号", "UpdatedEmoji", "cn.enaium.kookstarter.event.UpdatedEmoji"),
    GUILD_MEMBER_OFFLINE("公会成员离线", "GuildMemberOffline", "cn.enaium.kookstarter.event.GuildMemberOffline");

    /**
     * 标题
     */
    private final String title;

    /**
     * 名称
     */
    private final String name;

    /**
     * 全类名
     */
    private final String className;
}
