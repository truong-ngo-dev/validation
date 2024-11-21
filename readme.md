# Validation Library

---
### Description
Java library that provide dynamic validation for object

---
### Usage
- Configuration scan package for validation
```
    @Configuration
    @ComponentScan("com.nob.validation")
    public class ValidationConfiguration {
    }
```
- Define two property in `application.properties` like this:
```
    validation.model.package=package.content.your.validation.model
    validation.profile.directory=resource.directory.of.additional.validation.profile
```

- Either using annotation for auto generate validation meta like this:
```
    @Profile
    public class Project {

        @Constraint(operator = Operator.MIN_SIZE, comparedValues = {"5"}, message = "name must have at least 5 character")
        private String name;
        @Constraint(operator = Operator.MAX_SIZE, comparedValues = {"10"}, message = "code must have max 10 character")
        private String code;
        private String description;
        private Long totalValue;
        private String status;
        private String departmentId;
        private User user;
    }
```
or define a validation json file:
```
    {
        "profile": "project",
        "javaType": "com.nob.validation.v2.test.Project",
        "properties": [
            {
                "name": "name",
                "javaType": "java.lang.String",
                "criteria": [
                    {
                        "operator": "MIN_SIZE",
                        "comparedValues": ["12"],
                        "message": "Must be at least 12 character"
                    }
                ]
            }, 
            {
                "name": "code",
                "javaType": "java.lang.String",
                "criteria": [
                    {
                        "operator": "MAX_SIZE",
                        "comparedValues": ["12"],
                        "message": "Must be max 12 character"
                    }
                ]
            }
        ]
    }
```
- Annotated method and parameter need to be validated with `@Validated` and `@Valid`
```
    @Validated
    @PostMapping("/test-v1")
    public ResponseEntity<String> test(@RequestBody @Valid() Project body) {
        return ResponseEntity.ok("OK");
    }
```