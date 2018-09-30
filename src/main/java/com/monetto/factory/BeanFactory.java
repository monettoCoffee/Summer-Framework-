package com.monetto.factory;


import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * @author monetto
 */
public class BeanFactory {
    private HashMap<String, Object> singletonBeanFactory = new HashMap<String, Object>();
    private HashMap<String, SummerBean> xmlDefinationFactory = new HashMap<String, SummerBean>();
    private HashMap<String, SummerBean> annotationDefinationFactory = new HashMap<String, SummerBean>();

    public void instanceBeansFromXml(String contextPath) {
        try {
            List<Element> beanElements = new SAXReader().read(getClass().getClassLoader().getResourceAsStream(contextPath)).getRootElement().elements("bean");
            for (Element element : beanElements) {
                String beanId = element.attributeValue("id");
                String beanScope = element.attributeValue("scope");
                Class<?> clazz = Class.forName(element.attributeValue("class"));
                if (clazz != null) {
                    SummerBean summerBean = new SummerBean(clazz);
                    summerBean.setElement(element);
                    summerBean.setScope(beanScope == null ? "singleton" : beanScope);
                    summerBean.setId(beanId);
                    xmlDefinationFactory.put(beanId, summerBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void instanceBeansFromAnnotation(final String packageName) {
        File[] files = new File(getClass().getClassLoader().getResource(
                packageName.replaceAll("\\.", "/")
        ).getFile()).listFiles(
                new FileFilter() {
                    public boolean accept(File file) {
                        String fileName = file.getName();
                        if (file.isDirectory()) {
                            instanceBeansFromAnnotation(packageName + "." + fileName);
                        } else {
                            return fileName.endsWith(".class");
                        }
                        return false;
                    }
                });
        for (File file : files) {
            String fileName = file.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            String beanId = String.valueOf(fileName.charAt(0)).toLowerCase() + fileName.substring(1);
            try {
                Class<?> clazz = Class.forName(packageName + "." + fileName);
                if (clazz.isAnnotationPresent(Component.class)) {
                    SummerBean summerBean = new SummerBean(clazz);
                    summerBean.setId(beanId);
                    summerBean.setScope(clazz.getAnnotation(Component.class).scope());
                    annotationDefinationFactory.put(beanId, summerBean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T getBean(String beanId) {
        SummerBean summerBean;
        if ((summerBean = annotationDefinationFactory.get(beanId)) != null) {
            return (T) getAnnotationBean(summerBean);
        } else if ((summerBean = xmlDefinationFactory.get(beanId)) != null) {
            return (T) getXmlBean(summerBean);
        } else {
            return null;
        }
    }

    private <T> T getXmlBean(SummerBean summerBean) {
        String scope = summerBean.getScope();
        String beanId = summerBean.getId();
        Object object;
        try {
            if ("singleton".equals(scope)) {
                if ((object = singletonBeanFactory.get(beanId)) == null) {
                    object = setXmlFieldValues(summerBean);
                    singletonBeanFactory.put(beanId, object);
                }
                return (T) object;
            } else if ("prototype".equals(scope)) {
                return (T) setXmlFieldValues(summerBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> T getAnnotationBean(SummerBean summerBean) {
        Class<?> clazz = summerBean.getDefinition();
        String beanId = summerBean.getId();
        String scope = summerBean.getScope();
        Object object;
        try {
            if ("singleton".equals(scope)) {
                if ((object = singletonBeanFactory.get(beanId)) == null) {
                    object = setAnnotationFieldValues(clazz);
                    singletonBeanFactory.put(beanId, object);
                }
                return (T) object;
            } else if ("prototype".equals(scope)) {
                return (T) setAnnotationFieldValues(clazz);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object setXmlFieldValues(SummerBean summerBean) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        List<Element> properties = summerBean.getElement().elements("property");
        Class<?> clazz = summerBean.getDefinition();
        Object object = clazz.newInstance();
        if (properties != null) {
            for (Element property : properties) {
                String fieldValue = property.attributeValue("value");
                Field field = clazz.getDeclaredField(property.attributeValue("name"));
                field.setAccessible(true);
                String fieldType = field.getType().getName();
                if ("int".equals(fieldType) || "java.lang.Integer".equals(fieldType)) {
                    int intFieldValue = Integer.parseInt(fieldValue);
                    field.set(object, intFieldValue);
                } else if ("java.lang.String".equals(fieldType)) {
                    field.set(object, fieldValue);
                }
            }
        }
        return object;
    }

    private Object setAnnotationFieldValues(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        Object object = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Value.class)) {
                String fieldName = field.getName();
                fieldName = String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
                String value = field.getAnnotation(Value.class).value();
                String fieldType = field.getType().getName();
                try {
                    Method method = clazz.getDeclaredMethod("set" + fieldName, field.getType());
                    if ("int".equals(fieldType) || "java.lang.Integer".equals(fieldType)) {
                        method.invoke(object, Integer.valueOf(value));
                    } else if ("java.lang.String".equals(fieldType)) {
                        method.invoke(object, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (field.isAnnotationPresent(Autowired.class)) {
                Object fieldBean = getBean(field.getName());
                if (fieldBean != null) {
                    field.setAccessible(true);
                    field.set(object, fieldBean);
                }
            }
        }
        return object;
    }
}
