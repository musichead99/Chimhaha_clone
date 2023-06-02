package net.chimhaha.clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ChimhahaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChimhahaApplication.class, args);
    }

}
