package com.nob.validation.initializer;

import com.nob.utils.ClassUtils;
import com.nob.validation.annotation.Profile;
import com.nob.validation.metadata.context.ContextMetadataLoader;
import com.nob.validation.metadata.validation.ObjectValidationMetadata;
import com.nob.validation.metadata.validation.ValidationMetadataLoader;
import com.nob.validation.metadata.validation.ValidationMetadataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationDataInitializer {

    private final ContextMetadataLoader contextMetadataLoader;
    private final ValidationMetadataLoader validationMetaLoader;
    private final ValidationMetadataProvider validationMetaProvider;

    @Value("${validation.model.package}")
    private String scanPackage;

    @Value("${validation.profile.directory}")
    private String profileDirectory;

    public String[] getScanPackage() {
        return scanPackage.split(",");
    }

    @EventListener
    public void loadContextAndValidationMeta(ApplicationReadyEvent event) {
        try {
            for (String scanPackage : getScanPackage()) {
                List<Class<?>> classes = ClassUtils.scanPackage(scanPackage);
                for (Class<?> clazz : classes) {
                    if (clazz.isAnnotationPresent(Profile.class)) {
                        contextMetadataLoader.loadMetadata(clazz, clazz.getName());
                        String profile = clazz.getAnnotation(Profile.class).value();
                        validationMetaLoader.loadValidationMeta(clazz, profile);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        List<ObjectValidationMetadata> metas = ClassUtils.loadJson(profileDirectory, ObjectValidationMetadata.class);
        for (ObjectValidationMetadata meta : metas) {
            if (!validationMetaProvider.contains(meta.getJavaType(), meta.getProfile())) {
                validationMetaProvider.addValidation(meta);
            }
        }
    }
}
