package zut.cs.sys;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"zut.cs.*"})
@EnableSwagger2Doc
public class SysRunner {

    public static void main(String[] args) {
        SpringApplication.run(SysRunner.class, args);
    }
}