package com.example.demo;

import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ResourceUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/application-test.yml")
@TestPropertySource(locations = "classpath:/application-test.properties")
@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected UserRepository userRepository;
    protected TransactionTemplate transactionTemplate;

    @Autowired
    protected ResourceLoader resourceLoader;

    @Autowired
    protected PlatformTransactionManager platformTransactionManager;

    @AfterEach
    public void cleanUp() {
        withTransaction(() -> {
            userRepository.deleteAll();
        });
    }

    protected <T> T withTransaction(Callable<T> callable, Class<T> cls) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            try {
                return callable.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute(status -> {
            try {
                return callable.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    protected void withTransaction(Runnable runnable) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            runnable.run();
            return;
        }
        transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                runnable.run();
            }
        });
    }


    @SneakyThrows
    protected <T> T readResourceValue(String resourcePath, Class<T> tClass) {
        Resource resource = resourceLoader.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + resourcePath);
        return objectMapper.readValue(resource.getInputStream(), tClass);
    }

    @SneakyThrows
    protected <T> T readResourceValues(String resourcePath, TypeReference<T> typeReference) {
        Resource resource = resourceLoader.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + resourcePath);
        return objectMapper.readValue(resource.getInputStream(), typeReference);
    }

    @SneakyThrows
    protected String readResourceToString(String resourcePath) {
        Resource resource = resourceLoader.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + resourcePath);
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    protected String writeValueAsString(Object value) {
        return objectMapper.writeValueAsString(value);
    }


}