package br.com.picpay_challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PicpayChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PicpayChallengeApplication.class, args);
    }

}
