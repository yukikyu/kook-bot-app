package com.yukikyu.kook.boot.app;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author: yukikyu
 * @date: 2023-07-21 10:45
 */
@Slf4j
public class TempTest {

    @Test
    public void test01() {
        log.info(
            Map
                .of(
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
                    "            \"content\": \"在 (chn){}(chn) 发送组队信息，机器人会自动识别，并帮你发送邀请，无需复制链接。\"\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]"
                )
                .get("DEFAULT")
        );
    }
}
