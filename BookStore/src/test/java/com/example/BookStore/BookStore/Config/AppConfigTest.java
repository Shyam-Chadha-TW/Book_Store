package com.example.BookStore.BookStore.Config;

import com.example.BookStore.BookStore.Configs.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(AppConfig.class)
public class AppConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testModelMapperBeanIsLoaded() {
        ModelMapper modelMapper = context.getBean(ModelMapper.class);
        assertThat(modelMapper).isNotNull();
    }
}

