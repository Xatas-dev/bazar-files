package org.bazar.fw;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@ApplicationScoped
@RequiredArgsConstructor
public class InternalS3ClientConfig {
    @ConfigProperty(name = "settings.s3.internal-endpoint")
    String internalEndpoint;
    @ConfigProperty(name = "quarkus.s3.aws.region")
    String region;
    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.access-key-id")
    String accessKey;
    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.secret-access-key")
    String secretKey;

    @Produces
    @ApplicationScoped
    @InternalS3
    public S3Client internalS3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(internalEndpoint))
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .forcePathStyle(true)
                .build();
    }
}
