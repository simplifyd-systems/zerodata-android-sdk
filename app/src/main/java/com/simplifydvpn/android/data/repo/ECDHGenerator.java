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

    public void generateKey() throws Exception {
        Console console = System.console();
        // Generate ephemeral ECDH keypair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(256);
        KeyPair kp = kpg.generateKeyPair();
        byte[] ourPk = kp.getPublic().getEncoded();

        // Display our public key
        Log.d("ECDH", encodeToString(ourPk, 16));

        // Read other's public key:
        byte[] otherPk = decodeHex(console.readLine("Other PK: ").toCharArray());

        KeyFactory kf = KeyFactory.getInstance("EC");
        X509EncodedKeySpec pkSpec = new X509EncodedKeySpec(otherPk);
        PublicKey otherPublicKey = kf.generatePublic(pkSpec);

        // Perform key agreement
        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
        ka.init(kp.getPrivate());
        ka.doPhase(otherPublicKey, true);

        // Read shared secret
        byte[] sharedSecret = ka.generateSecret();
        Log.d("ECDH", decodeHex(sharedSecret.toString().toCharArray()).toString());

        // Derive a key from the shared secret and both public keys
        MessageDigest hash = MessageDigest.getInstance("SHA-256");
        hash.update(sharedSecret);
        // Simple deterministic ordering
        List<ByteBuffer> keys = Arrays.asList(ByteBuffer.wrap(ourPk), ByteBuffer.wrap(otherPk));
        Collections.sort(keys);
        hash.update(keys.get(0));
        hash.update(keys.get(1));

        byte[] derivedKey = hash.digest();
        Log.d("ECDH", encodeToString(derivedKey, 16));
    }
}
