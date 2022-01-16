package com.simplifydvpn.android.data.repo;

import java.security.*;
import java.security.spec.ECParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;

import java.util.*;
import java.nio.ByteBuffer;
import java.io.Console;

import static org.apache.commons.codec.binary.Hex.decodeHex;
import static android.util.Base64.encodeToString;

import android.util.Log;

public class ECDHGenerator {

    public KeyPair generateKeyPair() throws Exception {
        Console console = System.console();
        // Generate ephemeral ECDH keypair
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
