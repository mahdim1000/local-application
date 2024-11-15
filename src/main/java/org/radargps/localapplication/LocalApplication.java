package org.radargps.localapplication;

import org.radargps.localapplication.tcp.connection.handler.ServerManager;
import org.radargps.localapplication.common.util.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LocalApplication {

    public static void main(String[] args) throws Exception {
        Config.setConfig("debug.xml");
        var context = SpringApplication.run(LocalApplication.class, args);

        var serverManager = context.getBean(ServerManager.class);
        serverManager.start();
    }

}
