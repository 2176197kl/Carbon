package com.carbon.fabric;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;

public class PeerObj {
    private HFClient hfClient;
    private ChaincodeID chaincodeID;
    private Channel channel;

    // 构造函数
    public PeerObj(HFClient hfClient, ChaincodeID chaincodeID, Channel channel) {
        this.hfClient = hfClient;
        this.chaincodeID = chaincodeID;
        this.channel = channel;
    }

    // Getter 和 Setter 方法
    public HFClient getHfClient() {
        return hfClient;
    }

    public void setHfClient(HFClient hfClient) {
        this.hfClient = hfClient;
    }

    public ChaincodeID getChaincodeID() {
        return chaincodeID;
    }

    public void setChaincodeID(ChaincodeID chaincodeID) {
        this.chaincodeID = chaincodeID;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
