package com.monetto.entry;

import com.monetto.factory.Component;
import com.monetto.factory.Value;

/**
 * @author monetto
 */
@Component(scope = "prototype")
public class PrototypeStudent extends Student {
    @Value("3")
    private String id;
    @Value("33")
    private String name;
    @Value("333")
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
