package com.yukikyu.kook.boot.app.constant;

import java.util.List;
import java.util.Map;
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
        Map.of(
            "DEFAULT",
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
        ),
        List.of("//组队")
    ),
    HELP(
        "求助",
        Map.of(
            "ASK",
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
            "          \"content\": \"\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"section\",\n" +
            "        \"text\": {\n" +
            "          \"type\": \"plain-text\",\n" +
            "          \"content\": \"一键发送向大佬求助~\"\n" +
            "        },\n" +
            "        \"mode\": \"right\",\n" +
            "        \"accessory\": {\n" +
            "          \"type\": \"button\",\n" +
            "          \"theme\": \"primary\",\n" +
            "          \"click\": \"return-val\",\n" +
            "          \"value\": \"HELP\",\n" +
            "          \"text\": {\n" +
            "            \"type\": \"plain-text\",\n" +
            "            \"content\": \"立即求助\"\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"context\",\n" +
            "        \"elements\": [\n" +
            "          {\n" +
            "            \"type\": \"kmarkdown\",\n" +
            "            \"content\": \"在(chn){}(chn)发送求助信息，机器人会自动识别，点击求助按钮后将自动帮你推送给萌新导师，无需复制连接，只需在语音房间内等待萌新导师的到来！\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]",
            "WAITING",
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
            "          \"content\": \"\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"section\",\n" +
            "        \"text\": {\n" +
            "          \"type\": \"plain-text\",\n" +
            "          \"content\": \"一键发送向大佬求助~\"\n" +
            "        },\n" +
            "        \"mode\": \"right\",\n" +
            "        \"accessory\": {\n" +
            "          \"type\": \"button\",\n" +
            "          \"theme\": \"warning\",\n" +
            "          \"text\": {\n" +
            "            \"type\": \"plain-text\",\n" +
            "            \"content\": \"求助中\"\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"context\",\n" +
            "        \"elements\": [\n" +
            "          {\n" +
            "            \"type\": \"kmarkdown\",\n" +
            "            \"content\": \"在(chn){}(chn)发送求助信息，机器人会自动识别，点击求助按钮后将自动帮你推送给萌新导师，无需复制连接，只需在语音房间内等待萌新导师的到来！\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]",
            "HELP_MESSAGE",
            "[\n" +
            "  {\n" +
            "    \"type\": \"card\",\n" +
            "    \"theme\": \"secondary\",\n" +
            "    \"size\": \"lg\",\n" +
            "    \"modules\": [\n" +
            "      {\n" +
            "        \"type\": \"header\",\n" +
            "        \"text\": {\n" +
            "          \"type\": \"plain-text\",\n" +
            "          \"content\": \"有萌新向你求助~\"\n" +
            "        }\n" +
            "      },\n" +
            "      { \"type\": \"invite\", \"code\": \"{}\" }\n" +
            "    ]\n" +
            "  }\n" +
            "]"
        ),
        List.of("//求助", "萌新求助")
    );

    final String title;

    final Map<String, String> contentTemplate;

    final List<String> command;
}
