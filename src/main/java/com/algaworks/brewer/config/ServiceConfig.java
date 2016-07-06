package com.algaworks.brewer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.algaworks.brewer.service.CervejaService;

@Configuration
@ComponentScan(basePackageClasses = CervejaService.class)
public class ServiceConfig {

}
