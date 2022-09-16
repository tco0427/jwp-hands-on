package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        final Set<Class<?>> annotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        final Set<Class<?>> annotatedWithService = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> annotatedWithRepository = reflections.getTypesAnnotatedWith(Repository.class);

        log.info("{}", annotatedWithController);
        log.info("{}", annotatedWithService);
        log.info("{}", annotatedWithRepository);
    }
}
