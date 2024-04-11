package com.carbon.controller;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
//import com.carbon.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@RestController
@RequestMapping("/ai")
public class ChatController {

//    private final ChatService chatService;
//
//    @Autowired
//    public ChatController(ChatService chatService) {
//        this.chatService = chatService;
//    }
    @Value("${baidu.api.clientId}")
    private String clientId;

    @Value("${baidu.api.clientSecret}")
    private String clientSecret;

    // 百度 API 获取令牌的接口地址
    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
    @GetMapping("/chat")
    public Map<String, String> getAccessToken(@RequestParam String message) throws JsonProcessingException {
        // 构造获取令牌的请求参数
        String token_url = ACCESS_TOKEN_URL + "?grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
        // 发送 GET 请求获取令牌
//        RestTemplate restTemplate = new RestTemplate();
//        BaiduAccessTokenResponse response = restTemplate.getForObject(token_url, BaiduAccessTokenResponse.class);
//        System.out.println(response.getAccessToken());
        String chat_url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions";
        String accessToken = "24.3b542c13b5dbb209ead4bacb2dd46dc1.2592000.1715357689.282335-60570907";

        HashMap<String, String> msg = new HashMap<>();
        msg.put("role", "user");
        msg.put("content", message);

        ArrayList<HashMap> messages = new ArrayList<>();
        messages.add(msg);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);

        String response = HttpUtil.post(chat_url + "?access_token=" + accessToken, JSONUtil.toJsonStr(requestBody));
        // 解析响应并提取结果
        JsonParser parser = new JsonParser();
        JsonElement responseJsonElement = parser.parse(response);
        System.out.println(responseJsonElement);
        String result = responseJsonElement.getAsJsonObject().get("result").getAsString();

        // 创建并返回包含文心回复消息的Map对象
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("result", result);
        System.out.println(result);

        return resultMap;

    }
}
