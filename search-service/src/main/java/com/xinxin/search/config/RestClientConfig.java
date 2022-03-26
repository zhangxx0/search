package com.xinxin.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {

    String esConnect = "localhost:9200";
    String esUrl = "localhost";
    Integer esPort = 9200;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(esConnect)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public RestClient restClient() {
        // Create the low-level client
        final RestClient restClient = RestClient.builder(
                new HttpHost(esUrl, esPort)).build();
        return restClient;
    }
}
