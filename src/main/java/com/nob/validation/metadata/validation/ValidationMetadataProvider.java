package com.nob.validation.metadata.validation;

import com.nob.validation.exception.ValidationMetadataException;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Provide validation metadata
 * */
@Getter
@Component
public class ValidationMetadataProvider {

    /**
     * Holder of all validation metadata
     * */
    private final List<ObjectValidationMetadata> validations = new ArrayList<>();

    public ObjectValidationMetadata load(Class<?> javaType, String profile) {
        String prof = Objects.isNull(profile) || profile.isEmpty() ? "default" : profile;
        return validations.stream()
                .filter(v -> v.getJavaType().equals(javaType))
                .filter(v -> v.getProfile().equals(prof))
                .findFirst()
                .orElse(null);
    }

    public ObjectValidationMetadata loadDefault(Class<?> javaType) {
        return validations.stream()
                .filter(v -> v.getJavaType().equals(javaType))
                .filter(v -> v.getProfile().equals("default"))
                .findFirst()
                .orElseThrow(() -> new ValidationMetadataException("Can not find validation for " + javaType + " with profile default"));
    }

    public void addValidation(ObjectValidationMetadata validation) {
        ObjectValidationMetadata v = load(validation.getJavaType(), validation.getProfile());
        if (Objects.isNull(v)) validations.add(validation);
    }

    public boolean contains(Class<?> javaType, String profile) {
        return Objects.nonNull(load(javaType, profile));
    }
}
