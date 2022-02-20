package ru.neoflex.mjcryptobot.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerCfg {

    @Value("${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.group-id}")
    private String kafkaGroupId;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");

        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
//        props.put(SaslConfigs.SASL_JAAS_CONFIG, jaas);
        props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"7FVY3HRGJCBZUNK2\" password=\"/x38MoYO2bdykrJGPgcCihWfoMqm4V64icBLcPZzYHCVqGvC4BfDWEp5rG1f1C9v\";");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put("ssl.endpoint.identification.algorithm", "https");
        props.put("allow.auto.create.topics", false);
//        props.put("auto.offset.reset", "latest");
        props.put("auto.offset.reset", "earliest");

        return props;
    }
}
