//package org.bmsource.dwh.proxy;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Controller;
//
//@SpringBootApplication
//@EnableEurekaClient
//@EnableZuulProxy
//@Controller
//public class ProxyApplication {
//  public static void main(String[] args ) {
//    new SpringApplicationBuilder(ProxyApplication.class).web(WebApplicationType.SERVLET).run(args);
//  }
//
//  @Bean
//  public SimpleFilter simpleFilter() {
//    return new SimpleFilter();
//  }
//}
