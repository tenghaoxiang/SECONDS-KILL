package top.haibaraai.secondsKill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("top.haibaraai.secondsKill.mapper")
@SpringBootApplication
public class SecondsKillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondsKillApplication.class, args);
    }

}
