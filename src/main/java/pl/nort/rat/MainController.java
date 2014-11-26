package pl.nort.rat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

@Controller
@EnableAutoConfiguration
@ComponentScan
public class MainController {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainController.class, args);
    }
}
