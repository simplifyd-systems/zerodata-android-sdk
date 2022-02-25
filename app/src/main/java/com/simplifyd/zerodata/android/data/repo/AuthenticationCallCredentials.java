package com.simplifyd.zerodata.android.data.repo;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

import java.util.concurrent.Executor;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;

public class AuthenticationCallCredentials extends CallCredentials {
    private String token;
    public static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("jwt", ASCII_STRING_MARSHALLER);

    public AuthenticationCallCredentials(String token) {
        this.token = token;
    }

    @Override
    public void applyRequestMetadata(
            RequestInfo requestInfo,
            Executor executor,
            MetadataApplier metadataApplier) {
        executor.execute(() -> {
            try {
                Metadata headers = new Metadata();
                headers.put(AUTHORIZATION_METADATA_KEY, token);
                metadataApplier.apply(headers);
            } catch (Throwable e) {
                metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
            }
        });

    }

    @Override
    public void thisUsesUnstableApi() {
        // yes this is unstable :(
    }
}
