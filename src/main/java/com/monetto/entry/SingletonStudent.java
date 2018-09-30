package com.monetto.entry;

import com.monetto.factory.Component;
import com.monetto.factory.Value;

/**
 * @author monetto
 */
@Component(scope = "singleton")
public class SingletonStudent extends Student {
    @Value("2")
    private String id;
    @Value("22")
    private String name;
    @Value("222")
    private String password;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}
