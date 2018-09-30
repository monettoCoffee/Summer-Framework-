package com.monetto.factory;

import org.dom4j.Element;

/**
 * @author monetto
 */
public class SummerBean {
    private String id;
    private String scope;
    private Element element;
    private Class<?> definition;

    public SummerBean(Class<?> clazz) {
        this.definition = clazz;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Class<?> getDefinition() {
        return definition;
    }

    public void setDefinition(Class<?> definition) {
        this.definition = definition;
    }
}
