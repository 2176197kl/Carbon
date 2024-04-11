//package com.carbon.service;
//
//import com.alibaba.fastjson.JSON;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//@Service
//public class ChatService {
//
//    @Autowired
//    private BaiduApiUtil baiduApiUtil;
//
//    // 百度文心一言的接口地址
//    private static final String CHAT_API_ENDPOINT = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant";
//
//    public String sendMessageToBaidu(String message) throws UnsupportedEncodingException {
//        // 获取百度 API 的访问令牌
//        String accessToken = baiduApiUtil.getAccessToken();
//        //2、访问数据
//        String requestMethod = "POST";
//        String body = URLEncoder.encode("junshi","UTF-8");//设置要传的信息
//        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token="+accessToken;//post请求时格式
//        //测试：访问聚合数据的地区新闻api
//        HashMap<String, String> msg = new HashMap<>();
//        msg.put("role","user");
//        msg.put("content", message);
//        ArrayList<HashMap> messages = new ArrayList<>();
//        messages.add(msg);
//        HashMap<String, Object> requestBody = new HashMap<>();
//        requestBody.put("messages", messages);
//        String outputStr = JSON.toJSONString(requestBody);
//        JSON json = HttpRequest.httpRequest(url,requestMethod,outputStr,"application/json");
//        System.out.println(json);
////        // 构造百度文心一言的请求体
////        String requestBody = "{ \"query\": \"" + message + "\" }";
////
////        // 构造请求头
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON);
////        // 对 accessToken 进行 URL 编码
////        String encodedAccessToken = URLEncoder.encode(accessToken, "UTF-8");
////        headers.set("Authorization", "Bearer " + encodedAccessToken);
////
////
////        // 打印 accessToken
////        System.out.println("Access Token: " + accessToken);
////
////        // 发送 POST 请求
////        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
////        RestTemplate restTemplate = new RestTemplate();
////        ResponseEntity<String> response = restTemplate.postForEntity(CHAT_API_ENDPOINT, requestEntity, String.class);
////        System.out.println(response.getBody());
////        // 检查响应状态码
////        if (response.getStatusCode() == HttpStatus.OK) {
////            // 返回百度文心一言的回复消息
////            return response.getBody();
////        } else {
////            throw new RuntimeException("Failed to send message to Baidu Chat API");
////        }
//        return json.toJSONString();
//    }
//
//
//
//
//
//}
