package com.inglourious.overextension.annotation;


import com.inglourious.overextension.config.SpringOverextendConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.inglourious.overextension.fixture")
public class TestSpringOverextendConfiguration extends SpringOverextendConfiguration {
}
