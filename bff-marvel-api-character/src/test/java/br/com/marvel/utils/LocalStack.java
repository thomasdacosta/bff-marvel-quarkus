package br.com.marvel.utils;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

public class LocalStack implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        return null;
    }

    @Override
    public void stop() {

    }

}
