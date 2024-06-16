package com.northcoders.recordshopapi;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@EnableCaching
@SpringBootApplication
@EntityScan(basePackages = "com.northcoders.recordshopapi.models")
public class RecordshopapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordshopapiApplication.class, args);
	}

}

