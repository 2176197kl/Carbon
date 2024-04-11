package com.carbon.fabric;

public final class Const {
    //区块链网络中organizations的配置目录，从配置文件读取证书目录
    public static String BASE_PATH;

    // 节点的域名信息，域名解析在/etc/hosts
    public static final String PEER0_ORG1_DOMAIN_NAME = "47.120.7.215";
    public static final String PEER0_ORG2_DOMAIN_NAME = "peer0.org2.example.com";
    public static final String PEER0_ORG3_DOMAIN_NAME = "peer0.org3.example.com";
    public static final String PEER0_ORG4_DOMAIN_NAME = "peer0.org4.example.com";
    // 定义访问各节点的方式 grpcs 表示走grpc协议 + TLS, 后面域名 + 端口表示
    public static final String PEER0_ORG1_HOST = "grpcs://47.120.7.215:7051";
    public static final String PEER0_ORG2_HOST = "grpcs://peer0.org2.example.com:6051";
    public static final String PEER0_ORG3_HOST = "grpcs://peer0.org3.example.com:5051";
    public static final String PEER0_ORG4_HOST = "grpcs://peer0.org4.example.com:4051";
    // 组织1证书信息
    public static final String ORG1_TLS_DIR = "organizations/peerOrganizations/org1.example.com/tlsca/tlsca.org1.example.com-cert.pem";
    // 组织2证书信息
    public static final String ORG2_TLS_DIR = "organizations/peerOrganizations/org2.example.com/tlsca/tlsca.org2.example.com-cert.pem";
    // 组织3证书信息
    public static final String ORG3_TLS_DIR = "organizations/peerOrganizations/org3.example.com/tlsca/tlsca.org3.example.com-cert.pem";
    // 组织4证书信息
    public static final String ORG4_TLS_DIR = "organizations/peerOrganizations/org4.example.com/tlsca/tlsca.org4.example.com-cert.pem";
    // orderer 域名信息
    public static final String ORDERER0_DOMAIN_NAME = "orderer.example.com";
    // tls 证书信息
    public static final String ORDERER0_TLS_DIR = "organizations/ordererOrganizations/example.com/orderers/orderer.example.com/tls/ca.crt";
    public static final String ORDERER1_DOMAIN_NAME = "orderer1.example.com";
    public static final String ORDERER1_TLS_DIR = "organizations/ordererOrganizations/example.com/orderers/orderer1.example.com/tls/ca.crt";
    public static final String ORDERER2_DOMAIN_NAME = "orderer2.example.com";
    public static final String ORDERER2_TLS_DIR = "organizations/ordererOrganizations/example.com/orderers/orderer2.example.com/tls/ca.crt";
    public static final String ORDERER0_HOST = "grpcs://orderer.example.com:7050";
    public static final String ORDERER1_HOST = "grpcs://orderer1.example.com:6050";
    public static final String ORDERER2_HOST = "grpcs://orderer2.example.com:5050";

    //通道名称
    public static final String CHANNEL_NAME = "mychannel";
    //智能合约名称
    public static final String CHAINCODE_NAME = "carbon";
    //访问区块链网络使用哪个组织的秘钥
    public static final String USER1_MSP_ID="Org1MSP";
    //访问区块链网络使用的证书信息
//    public static final String USER1_KEY_FILE = "organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/priv_sk";
//    public static final String USER1_CERT_FILE = "organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem";
    public static final String USER1_KEY_FILE = "organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/priv_sk";
    public static final String USER1_CERT_FILE = "organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem";

    public static final String USER2_MSP_ID="Org2MSP";
    public static final String USER2_KEY_FILE = "organizations/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp/keystore/priv_sk";
    public static final String USER2_CERT_FILE = "organizations/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp/signcerts/Admin@org2.example.com-cert.pem";
    public static final String USER3_MSP_ID="Org3MSP";
    public static final String USER3_KEY_FILE = "organizations/peerOrganizations/org3.example.com/users/Admin@org3.example.com/msp/keystore/priv_sk";
    public static final String USER3_CERT_FILE = "organizations/peerOrganizations/org3.example.com/users/Admin@org3.example.com/msp/signcerts/Admin@org3.example.com-cert.pem";
    public static final String USER4_MSP_ID="Org4MSP";
    public static final String USER4_KEY_FILE = "organizations/peerOrganizations/org4.example.com/users/Admin@org4.example.com/msp/keystore/priv_sk";
    public static final String USER4_CERT_FILE = "organizations/peerOrganizations/org4.example.com/users/Admin@org4.example.com/msp/signcerts/Admin@org4.example.com-cert.pem";
}