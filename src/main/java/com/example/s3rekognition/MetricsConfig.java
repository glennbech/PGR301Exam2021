package com.example.s3rekognition;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.time.Duration;
import java.util.Map;

@Configuration
public class MetricsConfig {
    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient
                .builder()
                .region(Region.of("eu-west-1"))
                .build();
    }

    @Bean
    public MeterRegistry meterRegistry() {
        CloudWatchConfig cloudWatchConfig = setupCloudWatchConfig();
        return new CloudWatchMeterRegistry(
                cloudWatchConfig,
                Clock.SYSTEM,
                cloudWatchAsyncClient());
    }

    private CloudWatchConfig setupCloudWatchConfig() {
        return new CloudWatchConfig() {
            private final Map<String, String> configuration = Map.of(
                    "cloudwatch.namespace", "2021ConstructionSecurityCo",
                    "cloudwatch.step", Duration.ofMinutes(1).toString()
            );
        
            @Override
            public String get(String key) {
                return configuration.getOrDefault(key, null);
            }
        };
    }
}