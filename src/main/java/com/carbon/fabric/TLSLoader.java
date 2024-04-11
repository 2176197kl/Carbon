package com.carbon.fabric;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class TLSLoader {
    public static Properties loadTLSFile(String tlsDir, String domainName) throws Exception {
        Properties properties = new Properties();
        String certPath = tlsDir + "/" + domainName + "/tls.crt";
        String keyPath = tlsDir + "/" + domainName + "/tls.key";
        String rootCertPath = tlsDir + "/" + domainName + "/ca.crt";

        // 读取证书文件内容
        String certContent = new String(Files.readAllBytes(Paths.get(certPath)));
        String keyContent = new String(Files.readAllBytes(Paths.get(keyPath)));
        String rootCertContent = new String(Files.readAllBytes(Paths.get(rootCertPath)));

        // 设置TLS相关属性
        properties.put("clientCertFile", certContent);
        properties.put("clientKeyFile", keyContent);
        properties.put("pemFile", rootCertContent);

        return properties;
    }
}

