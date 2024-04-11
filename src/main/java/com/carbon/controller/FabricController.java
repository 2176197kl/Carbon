package com.carbon.controller;

import com.carbon.fabric.BlockChain;
import com.carbon.service.FabricService;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fabric")
public class FabricController {

    @Autowired
    FabricService fabricService;
    @Autowired
    BlockChain blockChain;
    @GetMapping("/query")
    public Map<String, String> queryBlockchain(//@RequestParam String peerName, @RequestParam String function, @RequestParam String[] args
    ) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            // 调用 BlockChain 服务中的 query 方法来查询区块链信息
            //47.120.7.215
            String queryResult = blockChain.query("47.120.7.215", "QueryUserInfo", new String[]{"1"});
            // 将查询结果放入结果 Map 中
            resultMap.put("result", queryResult);
        } catch (InvalidArgumentException | ProposalException e) {
            e.printStackTrace();
            // 在发生异常时将错误信息放入结果 Map 中
            resultMap.put("error", "Error occurred while querying blockchain: " + e.getMessage());
        }
        // 返回结果 Map 对象给前端
        return resultMap;
    }
}
