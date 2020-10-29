package saiernet.server.srnserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("saiernet.server.srnserver.dao")
public class SrnserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrnserverApplication.class, args);
	}
}
