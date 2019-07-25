package com.example.kvmmanger.config.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Exrickx
 */
@Data
@Configuration
//@ConfigurationProperties(prefix = "ignored")
public class IgnoredUrlsProperties {
    @Value("#{'${ignored.urls}'.split(',')}")
    private List<String> urls = new ArrayList<>();
}
