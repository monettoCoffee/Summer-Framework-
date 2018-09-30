package com.monetto.entry;

import com.monetto.factory.Autowired;
import com.monetto.factory.Component;

/**
 * @author monetto
 */
@Component(scope = "singleton")
public class StudentPack {
    @Autowired
    private SingletonStudent singletonStudent;

    public String test() {
        return this.singletonStudent.getPassword();
    }
}
