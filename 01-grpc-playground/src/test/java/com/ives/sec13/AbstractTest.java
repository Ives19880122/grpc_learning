package com.ives.sec13;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import com.ives.common.GrpcServer;

import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;

import java.nio.file.Path;
import java.security.KeyStore;
import java.util.concurrent.Callable;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractTest {

    private static final Path KEY_STORE = Path.of("src/test/resources/certs/grpc.keystore.jks");
    private static final Path TRUST_STORE = Path.of("src/test/resources/certs/grpc.truststore.jks");
    private static final char[] PASSWORD = "changeit".toCharArray();
    private final GrpcServer grpcServer = GrpcServer.create(6565, build->{
        build.addService(new BankService())
            .sslContext(serverSslContext());
    });

    @BeforeAll
    public void start(){
        grpcServer.start();
    }

    @AfterAll
    public void stop(){
        grpcServer.stop();
    }

    protected SslContext serverSslContext(){
        return handleException(()-> GrpcSslContexts.configure(SslContextBuilder.forServer(getKeyManagerFactory())).build());
    }

    protected SslContext clientSslContext(){
        return handleException(()-> GrpcSslContexts.configure(SslContextBuilder.forClient().trustManager(getTrustManagerFactory())).build());
    }

    protected KeyManagerFactory getKeyManagerFactory(){
        return handleException(()->{
            var kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            var keystore = KeyStore.getInstance(KEY_STORE.toFile(),PASSWORD);
            kmf.init(keystore,PASSWORD);
            return kmf;
        });
    }
    
    protected TrustManagerFactory getTrustManagerFactory(){
        return handleException(()->{
            var tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var trustStore = KeyStore.getInstance(TRUST_STORE.toFile(),PASSWORD);
            tmf.init(trustStore);
            return tmf;
        });
    }
    
    private <T> T handleException(Callable<T> callable){
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }       
    }
    

}