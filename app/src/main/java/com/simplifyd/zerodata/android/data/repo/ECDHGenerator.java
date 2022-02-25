package com.simplifyd.zerodata.android.data.repo;

import static org.apache.commons.codec.binary.Hex.decodeHex;

import android.util.Log;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;

public class ECDHGenerator {

    public KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(256);
        KeyPair kp = kpg.generateKeyPair();
        return kp;
    }

    public byte[] generateSharedSecret(KeyPair ourKp, byte[] serverPubKey) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("EC");
        X509EncodedKeySpec pkSpec = new X509EncodedKeySpec(serverPubKey);
        PublicKey serverPublicKey = kf.generatePublic(pkSpec);

        // Perform key agreement
        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
        ka.init(ourKp.getPrivate());
        ka.doPhase(serverPublicKey, true);

        // Read shared secret
        byte[] sharedSecret = ka.generateSecret();
        Log.d("ECDH", decodeHex(sharedSecret.toString().toCharArray()).toString());

        return sharedSecret;
    }
}
