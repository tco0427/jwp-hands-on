package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        Set<Object> beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            beans.add(createInstance(aClass));
        }
        return beans;
    }

    private static Object createInstance(final Class<?> aClass) {
        try {
            final Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException();
        }
    }

    private void setFields(final Object bean) {
        final Field[] fields = bean.getClass().getDeclaredFields();

        for (Field field : fields) {
            setBeanField(bean, field);
        }
    }

    private void setBeanField(final Object bean, final Field field) {
        try {
            injectField(bean, field);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void injectField(final Object bean, final Field field) throws IllegalAccessException {
        field.setAccessible(true);
        final Class<?> fieldType = field.getType();
        for (Object o : beans) {
            if (fieldType.isAssignableFrom(o.getClass())) {
                field.set(bean, o);
            }
        }
    }

    // 빈 컨텍스트(DI)에서 관리하는 빈을 찾아서 반환한다.
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
