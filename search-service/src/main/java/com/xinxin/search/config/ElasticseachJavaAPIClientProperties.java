package com.xinxin.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "com.xinxin.elasticseach")
public class ElasticseachJavaAPIClientProperties {
    private String uris;
    private Integer port;
}
