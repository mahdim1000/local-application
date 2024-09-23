package org.radargps.localapplication;

import org.radargps.localapplication.data_receiver.decode.ServerManager;
import org.radargps.localapplication.util.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class LocalApplication {

    public static void main(String[] args) throws Exception {
        Config.setConfig(args[0]);
        var context = SpringApplication.run(LocalApplication.class, args);

        var serverManager = context.getBean(ServerManager.class);
        serverManager.start();
    }

}
