package com.toushika.s3storagelearning.configuration;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    @Value("${config.aws.region}")
    private String region;
    @Value("${config.aws.s3.url}")
    private String s3EndpointUrl;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration(s3EndpointUrl))
                .enablePathStyleAccess()
                .build();
    }

    private AwsClientBuilder.EndpointConfiguration getEndpointConfiguration(String url) {
        return new AwsClientBuilder.EndpointConfiguration(url, region);
    }

}
