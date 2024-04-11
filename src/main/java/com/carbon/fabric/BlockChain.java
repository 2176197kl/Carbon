package com.carbon.fabric;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.carbon.fabric.TLSLoader.loadTLSFile;
@Service
public class BlockChain {
    private static HashMap<String, PeerObj> fabricMap;
    private static PeerObj init(String peerAndOrg) throws Exception {
        // 创建默认的加密套件，也就是与区块链网络连接时使用的非对称秘钥、哈希算法都是什么
        CryptoSuite suite = CryptoSuite.Factory.getCryptoSuite();
        // Hyperledger Fabric 客户端
        HFClient hfClient = HFClient.createNewInstance();
        // 设置密码算法
        hfClient.setCryptoSuite(suite);
        //fabUser 用于保存访问区块链时 使用的用户证书、私钥信息的上下文
        FabUser fabUser = null;
        if(peerAndOrg.equals(Const.PEER0_ORG1_DOMAIN_NAME)) {
            // 创建一个用户并加载证书与秘钥文件
            fabUser = new FabUser("admin", Const.USER1_MSP_ID, Const.USER1_KEY_FILE, Const.USER1_CERT_FILE);
        }
        if(peerAndOrg.equals(Const.PEER0_ORG2_DOMAIN_NAME)) {
            // 创建一个用户并加载证书与秘钥文件
            fabUser = new FabUser("admin", Const.USER2_MSP_ID, Const.USER2_KEY_FILE, Const.USER2_CERT_FILE);
        }
        if(peerAndOrg.equals(Const.PEER0_ORG3_DOMAIN_NAME)) {
            // 创建一个用户并加载证书与秘钥文件
            fabUser = new FabUser("admin", Const.USER3_MSP_ID, Const.USER3_KEY_FILE, Const.USER3_CERT_FILE);
        }
        if(peerAndOrg.equals(Const.PEER0_ORG4_DOMAIN_NAME)) {
            // 创建一个用户并加载证书与秘钥文件
            fabUser = new FabUser("admin", Const.USER4_MSP_ID, Const.USER4_KEY_FILE, Const.USER4_CERT_FILE);
        }
        //设置该客户端使用哪个用户的秘钥访问。
        hfClient.setUserContext((User) fabUser);
        // 创建通道实例
        Channel channel = hfClient.newChannel(Const.CHANNEL_NAME);
        //创建Peer对象
        Peer peer = null;
        if (peerAndOrg.equals(Const.PEER0_ORG1_DOMAIN_NAME)) {
            peer = hfClient.newPeer(Const.PEER0_ORG1_DOMAIN_NAME, Const.PEER0_ORG1_HOST,
                    loadTLSFile(Const.ORG1_TLS_DIR, Const.PEER0_ORG1_DOMAIN_NAME));
            channel.addPeer(peer);
        }
        if (peerAndOrg.equals(Const.PEER0_ORG2_DOMAIN_NAME)) {
            peer = hfClient.newPeer(Const.PEER0_ORG2_DOMAIN_NAME, Const.PEER0_ORG2_HOST,
                    loadTLSFile(Const.ORG2_TLS_DIR, Const.PEER0_ORG2_DOMAIN_NAME));
            channel.addPeer(peer);
        }
        if (peerAndOrg.equals(Const.PEER0_ORG3_DOMAIN_NAME)) {
            peer = hfClient.newPeer(Const.PEER0_ORG3_DOMAIN_NAME, Const.PEER0_ORG3_HOST,
                    loadTLSFile(Const.ORG3_TLS_DIR, Const.PEER0_ORG3_DOMAIN_NAME));
            channel.addPeer(peer);
        }
        if (peerAndOrg.equals(Const.PEER0_ORG4_DOMAIN_NAME)) {
            peer = hfClient.newPeer(Const.PEER0_ORG4_DOMAIN_NAME, Const.PEER0_ORG4_HOST,
                    loadTLSFile(Const.ORG4_TLS_DIR, Const.PEER0_ORG4_DOMAIN_NAME));
            channel.addPeer(peer);
        }
        //创建Orderer对象
        Orderer orderer0 = hfClient.newOrderer(Const.ORDERER0_DOMAIN_NAME, Const.ORDERER0_HOST,
                loadTLSFile(Const.ORDERER0_TLS_DIR, Const.ORDERER0_DOMAIN_NAME));
        Orderer orderer1 = hfClient.newOrderer(Const.ORDERER1_DOMAIN_NAME, Const.ORDERER1_HOST,
                loadTLSFile(Const.ORDERER1_TLS_DIR, Const.ORDERER1_DOMAIN_NAME));
        Orderer orderer2 = hfClient.newOrderer(Const.ORDERER2_DOMAIN_NAME, Const.ORDERER2_HOST,
                loadTLSFile(Const.ORDERER2_TLS_DIR, Const.ORDERER2_DOMAIN_NAME));
        channel.addOrderer(orderer0);
        channel.addOrderer(orderer1);
        channel.addOrderer(orderer2);
        // 通道初始化
        channel.initialize();
        // 创建访问链码的实例
        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(Const.CHAINCODE_NAME).build();
        PeerObj peerObj = new PeerObj(hfClient,chaincodeID,channel);
        return peerObj;
    }

    //后端程序分别与peer0.org1、peer0.org2、peer0.org3、peer0.org4建立连接，将Client句柄保存在fabricMap 中。
    public static void initConn(String basePath) {
        Const.BASE_PATH = basePath;
        fabricMap = new HashMap<String, PeerObj>();
        try {
            fabricMap.put(Const.PEER0_ORG1_DOMAIN_NAME, init(Const.PEER0_ORG1_DOMAIN_NAME));
            fabricMap.put(Const.PEER0_ORG2_DOMAIN_NAME,init(Const.PEER0_ORG2_DOMAIN_NAME));
            fabricMap.put(Const.PEER0_ORG3_DOMAIN_NAME,init(Const.PEER0_ORG3_DOMAIN_NAME));
            fabricMap.put(Const.PEER0_ORG4_DOMAIN_NAME,init(Const.PEER0_ORG4_DOMAIN_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //农产品服务调用send方法，将交易发送给区块链网络
//    public String send(String peerName, String function, String[] args) throws InvalidArgumentException, ProposalException {
//        PeerObj peerObj = fabricMap.get(peerName);
//        String message = null;
//        message = invoke(peerObj.getHfClient(), peerObj.getChannel(), peerObj.getChaincodeID(), function,
//                args);
//        return message;
//    }

    //农产品服务调用query方法，从区块链网络查询数据
    public String query(String peerName, String function, String[] args) throws InvalidArgumentException, ProposalException {
        PeerObj peerObj = fabricMap.get(peerName);
        String queryData = null;
        queryData = query(peerObj.getHfClient(), peerObj.getChannel(), peerObj.getChaincodeID(), function, args);
        return queryData;
    }

    private String query(HFClient hfClient, Channel channel, ChaincodeID chaincodeID, String function, String[] args) throws InvalidArgumentException, ProposalException {
        QueryByChaincodeRequest queryRequest = hfClient.newQueryProposalRequest();
        queryRequest.setChaincodeID(chaincodeID);
        queryRequest.setFcn(function);
        queryRequest.setArgs(args);

        Collection<ProposalResponse> queryResponses = channel.queryByChaincode(queryRequest);
        StringBuilder queryResult = new StringBuilder();

        for (ProposalResponse response : queryResponses) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                queryResult.append(response.getProposalResponse().getResponse().getPayload().toStringUtf8()).append("\n");
            } else {
                queryResult.append("Query failed with status: ").append(response.getStatus()).append("\n");
            }
        }
        System.out.println(queryRequest);
        return queryResult.toString();
    }

}
