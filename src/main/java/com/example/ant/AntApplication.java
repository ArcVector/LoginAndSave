package com.example.ant;

import com.example.ant.file.repository.FileRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
@EnableAutoConfiguration

public class AntApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AntApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

    }
}
