package com.borathings.borapagar.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements ApplicationRunner {

    @Autowired
    private ComponentFetchService componentFetchService;

    @Override
    public void run(ApplicationArguments args) {
        componentFetchService.fetchComponents();
    }
}
