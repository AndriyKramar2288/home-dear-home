package org.banew.hdh.fxapp;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionsUtils {

    public static JAXBContext createDynamicContext(String pack) throws JAXBException {
        Reflections reflections = new Reflections(pack, Scanners.SubTypes.filterResultsBy(s -> true));

        Class<?>[] classes = reflections.getSubTypesOf(Object.class).stream()
                .filter(clazz -> !clazz.getSimpleName().equals("package-info"))
                .toArray(Class<?>[]::new);

        return JAXBContext.newInstance(classes);
    }

    public static <T> Set<Class<? extends T>> getAllImplementations(Class<T> targetInterface) {
        // 1. Налаштовуємо сканування ВСЬОГО доступного classpath
        Reflections reflections = new Reflections("org.banew");

        // 2. Отримуємо всі підтипи
        Set<Class<? extends T>> allTypes = reflections.getSubTypesOf(targetInterface);

        // 3. Фільтруємо: прибираємо абстрактні класи та інтерфейси
        return allTypes.stream()
                .filter(clazz -> !clazz.isInterface())
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .collect(Collectors.toSet());
    }
}
