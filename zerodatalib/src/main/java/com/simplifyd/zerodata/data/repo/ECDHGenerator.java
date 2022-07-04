package com.simplifyd.zerodata.data.repo;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class ECDHGenerator {

    public KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(256);
        return kpg.generateKeyPair();
    }
}
