package com.simplifydvpn.android.data.repo;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyAgreement;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

public class ECDHGenerator {

    public KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(256);
        KeyPair kp = kpg.generateKeyPair();
        return kp;

/*
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
        keyPairGenerator.initialize(
                new KeyGenParameterSpec.Builder(
                        "eckeypair",
                        KeyProperties.PURPOSE_AGREE_KEY)
                        .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                        .build());
        KeyPair myKeyPair = keyPairGenerator.generateKeyPair();

        return myKeyPair;

 */
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
