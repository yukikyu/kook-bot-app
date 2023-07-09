package com.yukikyu.kook.boot.app.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 命令匹配类型
 *
 * @author: yukikyu
 * @date: 2023-07-08 15:03
 */
@Getter
@AllArgsConstructor
public enum KookCommandMatchType {
    FORM_A_TEAM(
        "组队",
        "[\n" +
        "  {\n" +
        "    \"type\": \"card\",\n" +
        "    \"theme\": \"secondary\",\n" +
        "    \"size\": \"lg\",\n" +
        "    \"modules\": [\n" +
        "      {\n" +
        "        \"type\": \"section\",\n" +
        "        \"text\": {\n" +
        "          \"type\": \"plain-text\",\n" +
        "          \"content\": \"社区成员正在寻找队友~\"\n" +
        "        }\n" +
        "      },\n" +
        "      { \"type\": \"invite\", \"code\": \"{}\" },\n" +
        "      {\n" +
        "        \"type\": \"context\",\n" +
        "        \"elements\": [\n" +
        "          {\n" +
        "            \"type\": \"kmarkdown\",\n" +
        "            \"content\": \"在(chn){}(chn)发送组队信息，机器人会自动识别，并帮你发送邀请，无需复制链接。\"\n" +
        "          }\n" +
        "        ]\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "]"
    );

    String title;

    String content;
}
