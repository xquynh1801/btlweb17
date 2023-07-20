package com.nhom7.qlkhachsan.app.database.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.nhom7.qlkhachsan.app.database.elasticsearch")
public class ElasticsearchConfig {
    @Value("${elasticsearch.host}")
    private String esHost;
    @Value("${elasticsearch.port}")
    private int esPort;
    @Value("${elasticsearch.clustername}")
    private String esClusterName;
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200") // Thiết lập địa chỉ Elasticsearch server
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
