package andreydem0505.remoteconfig.services;

import andreydem0505.remoteconfig.data.documents.PropertyType;
import andreydem0505.remoteconfig.exceptions.DynPropertyContextValidationException;
import andreydem0505.remoteconfig.exceptions.DynPropertyDataValidationException;
import andreydem0505.remoteconfig.exceptions.DynPropertyNameValidationException;
import andreydem0505.remoteconfig.exceptions.DynPropertyTypeValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DynPropertyValidationServiceTest extends TestBase {

    @Autowired
    private DynPropertyValidationService validationService;

    // validateName tests
    @ParameterizedTest
    @ValueSource(strings = {
            "simple_property",
            "property123",
            "UPPER_CASE_PROPERTY",
            "camelCaseProperty",
            "snake_case_property",
            "property.with.dots",
            "app.config.database.host",
            "feature_flag_v2",
            "p",
            "property_123_test"
    })
    void testValidateName_WhenValidPropertyName_ThenDoesNotThrowException(String propertyName) {
        assertDoesNotThrow(
                () -> validationService.validateName(propertyName),
                "Valid property name should not throw exception"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "property-with-dash",
            "property with space",
            "property@special",
            "property!invalid",
            ".starts.with.dot",
            "ends.with.dot.",
            "double..dots",
            "property/slash",
            "property\\backslash",
            "property#hash",
            "property$dollar",
            "property%percent",
            "property&ampersand",
            "property*asterisk",
            "property(parenthesis",
            "property)parenthesis",
            "property+plus",
            "property=equals",
            "property[bracket",
            "property]bracket",
            "property{brace",
            "property}brace",
            "property|pipe",
            "property:colon",
            "property;semicolon",
            "property'quote",
            "property\"doublequote",
            "property<less",
            "property>greater",
            "property,comma",
            "property?question",
            ""
    })
    void testValidateName_WhenInvalidPropertyName_ThenThrowsValidationException(String invalidPropertyName) {
        assertThrows(
                DynPropertyNameValidationException.class,
                () -> validationService.validateName(invalidPropertyName),
                "Invalid property name should throw validation exception"
        );
    }

    @Test
    void testValidateName_WhenNullPropertyName_ThenThrowsValidationException() {
        assertThrows(
                DynPropertyNameValidationException.class,
                () -> validationService.validateName(null),
                "Null property name should throw validation exception"
        );
    }

    // validateData tests for CUSTOM_PROPERTY
    @ParameterizedTest(name = "CUSTOM_PROPERTY with {1} should be valid")
    @MethodSource("provideCustomPropertyData")
    void testValidateData_WhenCustomPropertyWithValidData_ThenDoesNotThrowException(Object data, String type) {
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.CUSTOM_PROPERTY, data),
                "CUSTOM_PROPERTY with %s should be valid".formatted(type)
        );
    }

    private static Stream<Arguments> provideCustomPropertyData() {
        return Stream.of(
                Arguments.of("localhost:8080", "database connection string"),
                Arguments.of(3000, "port number"),
                Arguments.of(0.95, "success rate threshold"),
                Arguments.of(true, "feature enabled flag"),
                Arguments.of(new Date(), "last modified timestamp"),
                Arguments.of(Arrays.asList("admin", "moderator", "user"), "allowed roles list"),
                Arguments.of(Map.of("timeout", "30s", "retries", "3"), "service configuration map"),
                Arguments.of(null, "null value for optional config")
        );
    }

    // validateData tests for BOOLEAN_FEATURE_FLAG
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testValidateData_WhenBooleanFlagWithValidData_ThenDoesNotThrowException(boolean data) {
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.BOOLEAN_FEATURE_FLAG, data),
                "BOOLEAN_FEATURE_FLAG with %b should be valid".formatted(data)
        );
    }

    @ParameterizedTest(name = "BOOLEAN_FEATURE_FLAG with {1} should throw exception")
    @MethodSource("provideBooleanFlagInvalidDataTypes")
    void testValidateData_WhenBooleanFlagWithInvalidData_ThenThrowsValidationException(Object data, String type) {
        assertThrows(
                DynPropertyDataValidationException.class,
                () -> validationService.validateData(PropertyType.BOOLEAN_FEATURE_FLAG, data),
                "BOOLEAN_FEATURE_FLAG with %s should throw validation exception".formatted(type)
        );
    }

    private static Stream<Arguments> provideBooleanFlagInvalidDataTypes() {
        return Stream.of(
                Arguments.of("true", "string"),
                Arguments.of(1, "integer"),
                Arguments.of(null, "null")
        );
    }

    // validateData tests for PERCENTAGE_FEATURE_FLAG
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 25, 50, 75, 99, 100})
    void testValidateData_WhenPercentageFlagWithValidValue_ThenDoesNotThrowException(Integer percentage) {
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.PERCENTAGE_FEATURE_FLAG, percentage),
                "PERCENTAGE_FEATURE_FLAG with value %d should be valid".formatted(percentage)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 101, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void testValidateData_WhenPercentageFlagOutOfRange_ThenThrowsValidationException(Integer invalidPercentage) {
        assertThrows(
                DynPropertyDataValidationException.class,
                () -> validationService.validateData(PropertyType.PERCENTAGE_FEATURE_FLAG, invalidPercentage),
                "PERCENTAGE_FEATURE_FLAG with out of range value should throw validation exception"
        );
    }

    @ParameterizedTest(name = "PERCENTAGE_FEATURE_FLAG with {1} should throw exception")
    @MethodSource("providePercentageFlagInvalidDataTypes")
    void testValidateData_WhenPercentageFlagWithInvalidData_ThenThrowsValidationException(Object data, String type) {
        assertThrows(
                DynPropertyDataValidationException.class,
                () -> validationService.validateData(PropertyType.PERCENTAGE_FEATURE_FLAG, data),
                "PERCENTAGE_FEATURE_FLAG with %s should throw validation exception".formatted(type)
        );
    }

    private static Stream<Arguments> providePercentageFlagInvalidDataTypes() {
        return Stream.of(
                Arguments.of("50", "string"),
                Arguments.of(50.5, "double"),
                Arguments.of(null, "null")
        );
    }

    // validateData tests for EQUALITY_FEATURE_FLAG
    @ParameterizedTest(name = "EQUALITY_FEATURE_FLAG with {1} should be valid")
    @MethodSource("provideEqualityFlagData")
    void testValidateData_WhenEqualityFlagWithValidData_ThenDoesNotThrowException(Object data, String type) {
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.EQUALITY_FEATURE_FLAG, data),
                "EQUALITY_FEATURE_FLAG with %s should be valid".formatted(type)
        );
    }

    private static Stream<Arguments> provideEqualityFlagData() {
        return Stream.of(
                Arguments.of("production", "environment name"),
                Arguments.of(5, "API version number"),
                Arguments.of(null, "null for no restriction")
        );
    }

    // validateData tests for UNIT_IN_LIST_FEATURE_FLAG
    @Test
    void testValidateData_WhenUnitInListFlagWithStringList_ThenDoesNotThrowException() {
        List<String> allowedUsers = Arrays.asList("alice@example.com", "bob@example.com", "charlie@example.com");
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.UNIT_IN_LIST_FEATURE_FLAG, allowedUsers),
                "UNIT_IN_LIST_FEATURE_FLAG with string list should be valid"
        );
    }

    @Test
    void testValidateData_WhenUnitInListFlagWithIntegerList_ThenDoesNotThrowException() {
        List<Integer> allowedUserIds = Arrays.asList(1001, 1002, 1003);
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.UNIT_IN_LIST_FEATURE_FLAG, allowedUserIds),
                "UNIT_IN_LIST_FEATURE_FLAG with integer list should be valid"
        );
    }

    @Test
    void testValidateData_WhenUnitInListFlagWithEmptyList_ThenDoesNotThrowException() {
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.UNIT_IN_LIST_FEATURE_FLAG, Collections.emptyList()),
                "UNIT_IN_LIST_FEATURE_FLAG with empty list should be valid"
        );
    }

    @ParameterizedTest(name = "UNIT_IN_LIST_FEATURE_FLAG with {1} should throw exception")
    @MethodSource("provideListFlagInvalidData")
    void testValidateData_WhenUnitInListFlagWithInvalidData_ThenThrowsValidationException(Object data, String type) {
        assertThrows(
                DynPropertyDataValidationException.class,
                () -> validationService.validateData(PropertyType.UNIT_IN_LIST_FEATURE_FLAG, data),
                "UNIT_IN_LIST_FEATURE_FLAG with %s should throw validation exception".formatted(type)
        );
    }

    private static Stream<Arguments> provideListFlagInvalidData() {
        return Stream.of(
                Arguments.of("not_a_list", "string"),
                Arguments.of(123, "integer"),
                Arguments.of(null, "null")
        );
    }

    // validateIsFeatureFlag tests
    @ParameterizedTest(name = "{0} should be valid feature flag")
    @MethodSource("provideFeatureFlags")
    void testValidateIsFeatureFlag_WhenFeatureFlag_ThenDoesNotThrowException(PropertyType type) {
        assertDoesNotThrow(
                () -> validationService.validateIsFeatureFlag(type),
                "%s should be valid feature flag".formatted(type.name())
        );
    }

    private static Stream<PropertyType> provideFeatureFlags() {
        return Stream.of(
                PropertyType.BOOLEAN_FEATURE_FLAG,
                PropertyType.PERCENTAGE_FEATURE_FLAG,
                PropertyType.EQUALITY_FEATURE_FLAG,
                PropertyType.UNIT_IN_LIST_FEATURE_FLAG
        );
    }

    @Test
    void testValidateIsFeatureFlag_WhenCustomProperty_ThenThrowsValidationException() {
        assertThrows(
                DynPropertyTypeValidationException.class,
                () -> validationService.validateIsFeatureFlag(PropertyType.CUSTOM_PROPERTY),
                "CUSTOM_PROPERTY should not be valid feature flag"
        );
    }

    // validateContext tests for BOOLEAN_FEATURE_FLAG
    @Test
    void testValidateContext_WhenBooleanFlagWithNullContext_ThenDoesNotThrowException() {
        assertDoesNotThrow(
                () -> validationService.validateContext("enable_new_ui", PropertyType.BOOLEAN_FEATURE_FLAG,
                        null, true),
                "BOOLEAN_FEATURE_FLAG with null context should be valid"
        );
    }

    @Test
    void testValidateContext_WhenBooleanFlagWithNonNullContext_ThenThrowsValidationException() {
        assertThrows(
                DynPropertyContextValidationException.class,
                () -> validationService.validateContext("enable_new_ui", PropertyType.BOOLEAN_FEATURE_FLAG,
                        "some_context", true),
                "BOOLEAN_FEATURE_FLAG with non-null context should throw validation exception"
        );
    }

    // validateContext tests for PERCENTAGE_FEATURE_FLAG
    @Test
    void testValidateContext_WhenPercentageFlagWithNullContext_ThenDoesNotThrowException() {
        assertDoesNotThrow(
                () -> validationService.validateContext("beta_rollout", PropertyType.PERCENTAGE_FEATURE_FLAG,
                        null, 50),
                "PERCENTAGE_FEATURE_FLAG with null context should be valid"
        );
    }

    @Test
    void testValidateContext_WhenPercentageFlagWithNonNullContext_ThenThrowsValidationException() {
        assertThrows(
                DynPropertyContextValidationException.class,
                () -> validationService.validateContext("beta_rollout", PropertyType.PERCENTAGE_FEATURE_FLAG,
                        "user123", 50),
                "PERCENTAGE_FEATURE_FLAG with non-null context should throw validation exception"
        );
    }

    // validateContext tests for EQUALITY_FEATURE_FLAG
    @ParameterizedTest(name = "EQUALITY_FEATURE_FLAG with {2} context and {1} data should be valid")
    @MethodSource("provideEqualityFlagContextAndData")
    void testValidateContext_WhenEqualityFlagWithValidData_ThenDoesNotThrowException(
            Object context,
            Object data,
            String type
    ) {
        assertDoesNotThrow(
                () -> validationService.validateContext("environment_check", PropertyType.EQUALITY_FEATURE_FLAG,
                        context, data),
                "EQUALITY_FEATURE_FLAG with %s context should be valid".formatted(type)
        );
    }

    private static Stream<Arguments> provideEqualityFlagContextAndData() {
        return Stream.of(
                Arguments.of("production", "production", "matching string"),
                Arguments.of("staging", "production", "different string"),
                Arguments.of(5, 5, "matching integer"),
                Arguments.of(3, 5, "different integer"),
                Arguments.of(null, "production", "null context")
        );
    }

    // validateContext tests for UNIT_IN_LIST_FEATURE_FLAG
    @Test
    void testValidateContext_WhenUnitInListFlagWithMatchingStringType_ThenDoesNotThrowException() {
        List<String> allowedEmails = Arrays.asList("admin@example.com", "moderator@example.com", "test@example.com");
        assertDoesNotThrow(
                () -> validationService.validateContext("whitelist_access", PropertyType.UNIT_IN_LIST_FEATURE_FLAG,
                        "admin@example.com", allowedEmails),
                "UNIT_IN_LIST_FEATURE_FLAG with matching string context type should be valid"
        );
    }

    @Test
    void testValidateContext_WhenUnitInListFlagWithMatchingIntegerType_ThenDoesNotThrowException() {
        List<Integer> allowedUserIds = Arrays.asList(1001, 1002, 1003);
        assertDoesNotThrow(
                () -> validationService.validateContext("user_id_whitelist", PropertyType.UNIT_IN_LIST_FEATURE_FLAG,
                        1001, allowedUserIds),
                "UNIT_IN_LIST_FEATURE_FLAG with matching integer context type should be valid"
        );
    }

    @Test
    void testValidateContext_WhenUnitInListFlagWithEmptyList_ThenDoesNotThrowException() {
        assertDoesNotThrow(
                () -> validationService.validateContext("empty_whitelist", PropertyType.UNIT_IN_LIST_FEATURE_FLAG,
                        "any_user@example.com", Collections.emptyList()),
                "UNIT_IN_LIST_FEATURE_FLAG with empty list should be valid"
        );
    }

    @Test
    void testValidateContext_WhenUnitInListFlagWithMismatchedType_ThenThrowsValidationException() {
        List<String> allowedEmails = Arrays.asList("admin@example.com", "moderator@example.com", "test@example.com");
        assertThrows(
                DynPropertyContextValidationException.class,
                () -> validationService.validateContext("whitelist_access", PropertyType.UNIT_IN_LIST_FEATURE_FLAG,
                        123, allowedEmails),
                "UNIT_IN_LIST_FEATURE_FLAG with mismatched context type should throw validation exception"
        );
    }

    // validateContext tests for CUSTOM_PROPERTY
    @Test
    void testValidateContext_WhenCustomPropertyWithContext_ThenThrowsValidationException() {
        assertThrows(
                DynPropertyContextValidationException.class,
                () -> validationService.validateContext("custom_config", PropertyType.CUSTOM_PROPERTY,
                        "some_context", "some_value"),
                "CUSTOM_PROPERTY should not support context and should throw validation exception"
        );
    }

    @Test
    void testValidateContext_WhenCustomPropertyWithNullContext_ThenThrowsValidationException() {
        assertThrows(
                DynPropertyContextValidationException.class,
                () -> validationService.validateContext("custom_config", PropertyType.CUSTOM_PROPERTY,
                        null, "some_value"),
                "CUSTOM_PROPERTY should not support context even when null"
        );
    }

    // validateData tests for ALL_IN_LIST_FEATURE_FLAG
    @Test
    void testValidateData_WhenAllInListFlagWithStringList_ThenDoesNotThrowException() {
        List<String> allowedPermissions = Arrays.asList("read", "write", "delete");
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.ALL_IN_LIST_FEATURE_FLAG, allowedPermissions),
                "ALL_IN_LIST_FEATURE_FLAG with string list should be valid"
        );
    }

    @Test
    void testValidateData_WhenAllInListFlagWithIntegerList_ThenDoesNotThrowException() {
        List<Integer> allowedIds = Arrays.asList(100, 200, 300);
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.ALL_IN_LIST_FEATURE_FLAG, allowedIds),
                "ALL_IN_LIST_FEATURE_FLAG with integer list should be valid"
        );
    }

    @Test
    void testValidateData_WhenAllInListFlagWithEmptyList_ThenDoesNotThrowException() {
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.ALL_IN_LIST_FEATURE_FLAG, Collections.emptyList()),
                "ALL_IN_LIST_FEATURE_FLAG with empty list should be valid"
        );
    }

    @ParameterizedTest(name = "ALL_IN_LIST_FEATURE_FLAG with {1} should throw exception")
    @MethodSource("provideAllInListFlagInvalidData")
    void testValidateData_WhenAllInListFlagWithInvalidData_ThenThrowsValidationException(Object data, String type) {
        assertThrows(
                DynPropertyDataValidationException.class,
                () -> validationService.validateData(PropertyType.ALL_IN_LIST_FEATURE_FLAG, data),
                "ALL_IN_LIST_FEATURE_FLAG with %s should throw validation exception".formatted(type)
        );
    }

    private static Stream<Arguments> provideAllInListFlagInvalidData() {
        return Stream.of(
                Arguments.of("not_a_list", "string"),
                Arguments.of(42, "integer"),
                Arguments.of(null, "null")
        );
    }

    // validateData tests for ANY_IN_LIST_FEATURE_FLAG
    @Test
    void testValidateData_WhenAnyInListFlagWithStringList_ThenDoesNotThrowException() {
        List<String> allowedStatuses = Arrays.asList("active", "pending", "archived");
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.ANY_IN_LIST_FEATURE_FLAG, allowedStatuses),
                "ANY_IN_LIST_FEATURE_FLAG with string list should be valid"
        );
    }

    @Test
    void testValidateData_WhenAnyInListFlagWithIntegerList_ThenDoesNotThrowException() {
        List<Integer> allowedPriorities = Arrays.asList(1, 2, 3, 4, 5);
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.ANY_IN_LIST_FEATURE_FLAG, allowedPriorities),
                "ANY_IN_LIST_FEATURE_FLAG with integer list should be valid"
        );
    }

    @Test
    void testValidateData_WhenAnyInListFlagWithEmptyList_ThenDoesNotThrowException() {
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.ANY_IN_LIST_FEATURE_FLAG, Collections.emptyList()),
                "ANY_IN_LIST_FEATURE_FLAG with empty list should be valid"
        );
    }

    @ParameterizedTest(name = "ANY_IN_LIST_FEATURE_FLAG with {1} should throw exception")
    @MethodSource("provideAnyInListFlagInvalidData")
    void testValidateData_WhenAnyInListFlagWithInvalidData_ThenThrowsValidationException(Object data, String type) {
        assertThrows(
                DynPropertyDataValidationException.class,
                () -> validationService.validateData(PropertyType.ANY_IN_LIST_FEATURE_FLAG, data),
                "ANY_IN_LIST_FEATURE_FLAG with %s should throw validation exception".formatted(type)
        );
    }

    private static Stream<Arguments> provideAnyInListFlagInvalidData() {
        return Stream.of(
                Arguments.of("not_a_list", "string"),
                Arguments.of(99, "integer"),
                Arguments.of(null, "null")
        );
    }

    // validateData tests for STRING_CONTAINS_FEATURE_FLAG
    @ParameterizedTest
    @ValueSource(strings = {
            "localhost",
            "production",
            "feature-flag-pattern",
            "simple",
            "a",
            ""
    })
    void testValidateData_WhenStringContainsFlagWithValidString_ThenDoesNotThrowException(String data) {
        assertDoesNotThrow(
                () -> validationService.validateData(PropertyType.STRING_CONTAINS_FEATURE_FLAG, data),
                "STRING_CONTAINS_FEATURE_FLAG with string should be valid"
        );
    }

    @ParameterizedTest(name = "STRING_CONTAINS_FEATURE_FLAG with {1} should throw exception")
    @MethodSource("provideStringContainsFlagInvalidData")
    void testValidateData_WhenStringContainsFlagWithInvalidData_ThenThrowsValidationException(Object data, String type) {
        assertThrows(
                DynPropertyDataValidationException.class,
                () -> validationService.validateData(PropertyType.STRING_CONTAINS_FEATURE_FLAG, data),
                "STRING_CONTAINS_FEATURE_FLAG with %s should throw validation exception".formatted(type)
        );
    }

    private static Stream<Arguments> provideStringContainsFlagInvalidData() {
        return Stream.of(
                Arguments.of(123, "integer"),
                Arguments.of(true, "boolean"),
                Arguments.of(Arrays.asList("a", "b"), "list"),
                Arguments.of(null, "null")
        );
    }

    // validateContext tests for ALL_IN_LIST_FEATURE_FLAG
    @Test
    void testValidateContext_WhenAllInListFlagWithMatchingStringType_ThenDoesNotThrowException() {
        List<String> requiredPermissions = Arrays.asList("read", "write", "delete");
        assertDoesNotThrow(
                () -> validationService.validateContext("required_permissions", PropertyType.ALL_IN_LIST_FEATURE_FLAG,
                        List.of("read"), requiredPermissions),
                "ALL_IN_LIST_FEATURE_FLAG with matching string context type should be valid"
        );
    }

    @Test
    void testValidateContext_WhenAllInListFlagWithMatchingIntegerType_ThenDoesNotThrowException() {
        List<Integer> requiredRoleIds = Arrays.asList(1, 2, 3);
        assertDoesNotThrow(
                () -> validationService.validateContext("required_roles", PropertyType.ALL_IN_LIST_FEATURE_FLAG,
                        List.of(1), requiredRoleIds),
                "ALL_IN_LIST_FEATURE_FLAG with matching integer context type should be valid"
        );
    }

    @Test
    void testValidateContext_WhenAllInListFlagWithEmptyList_ThenDoesNotThrowException() {
        assertDoesNotThrow(
                () -> validationService.validateContext("any_permission", PropertyType.ALL_IN_LIST_FEATURE_FLAG,
                        List.of("admin"), Collections.emptyList()),
                "ALL_IN_LIST_FEATURE_FLAG with empty list should be valid"
        );
    }

    @Test
    void testValidateContext_WhenAllInListFlagWithMismatchedType_ThenThrowsValidationException() {
        List<String> requiredPermissions = Arrays.asList("read", "write", "delete");
        assertThrows(
                DynPropertyContextValidationException.class,
                () -> validationService.validateContext("required_permissions", PropertyType.ALL_IN_LIST_FEATURE_FLAG,
                        100, requiredPermissions),
                "ALL_IN_LIST_FEATURE_FLAG with mismatched context type should throw validation exception"
        );
    }

    // validateContext tests for ANY_IN_LIST_FEATURE_FLAG
    @Test
    void testValidateContext_WhenAnyInListFlagWithMatchingStringType_ThenDoesNotThrowException() {
        List<String> allowedStatuses = Arrays.asList("active", "pending", "completed");
        assertDoesNotThrow(
                () -> validationService.validateContext("user_status", PropertyType.ANY_IN_LIST_FEATURE_FLAG,
                        List.of("active"), allowedStatuses),
                "ANY_IN_LIST_FEATURE_FLAG with matching string context type should be valid"
        );
    }

    @Test
    void testValidateContext_WhenAnyInListFlagWithMatchingIntegerType_ThenDoesNotThrowException() {
        List<Integer> allowedScores = Arrays.asList(80, 85, 90, 95, 100);
        assertDoesNotThrow(
                () -> validationService.validateContext("minimum_score", PropertyType.ANY_IN_LIST_FEATURE_FLAG,
                        List.of(85), allowedScores),
                "ANY_IN_LIST_FEATURE_FLAG with matching integer context type should be valid"
        );
    }

    @Test
    void testValidateContext_WhenAnyInListFlagWithEmptyList_ThenDoesNotThrowException() {
        assertDoesNotThrow(
                () -> validationService.validateContext("any_status", PropertyType.ANY_IN_LIST_FEATURE_FLAG,
                        List.of("any_value"), Collections.emptyList()),
                "ANY_IN_LIST_FEATURE_FLAG with empty list should be valid"
        );
    }

    @Test
    void testValidateContext_WhenAnyInListFlagWithMismatchedType_ThenThrowsValidationException() {
        List<String> allowedCountries = Arrays.asList("US", "UK", "CA");
        assertThrows(
                DynPropertyContextValidationException.class,
                () -> validationService.validateContext("user_country", PropertyType.ANY_IN_LIST_FEATURE_FLAG,
                        42, allowedCountries),
                "ANY_IN_LIST_FEATURE_FLAG with mismatched context type should throw validation exception"
        );
    }

    // validateContext tests for STRING_CONTAINS_FEATURE_FLAG
    @ParameterizedTest(name = "STRING_CONTAINS_FEATURE_FLAG with string context ''{0}'' should be valid")
    @MethodSource("provideStringContainsFlagContextData")
    void testValidateContext_WhenStringContainsFlagWithValidContext_ThenDoesNotThrowException(String context) {
        assertDoesNotThrow(
                () -> validationService.validateContext("path_pattern", PropertyType.STRING_CONTAINS_FEATURE_FLAG,
                        context, "/api/v1"),
                "STRING_CONTAINS_FEATURE_FLAG with string context should be valid"
        );
    }

    private static Stream<String> provideStringContainsFlagContextData() {
        return Stream.of(
                "localhost",
                "production-api",
                "path/to/resource",
                "email@example.com",
                ""
        );
    }

    @ParameterizedTest(name = "STRING_CONTAINS_FEATURE_FLAG with {1} context should throw exception")
    @MethodSource("provideStringContainsFlagInvalidContext")
    void testValidateContext_WhenStringContainsFlagWithInvalidContext_ThenThrowsValidationException(
            Object context, String type) {
        assertThrows(
                DynPropertyContextValidationException.class,
                () -> validationService.validateContext("pattern_check", PropertyType.STRING_CONTAINS_FEATURE_FLAG,
                        context, "pattern_value"),
                "STRING_CONTAINS_FEATURE_FLAG with %s context should throw validation exception".formatted(type)
        );
    }

    private static Stream<Arguments> provideStringContainsFlagInvalidContext() {
        return Stream.of(
                Arguments.of(123, "integer"),
                Arguments.of(true, "boolean"),
                Arguments.of(Arrays.asList("a", "b"), "list"),
                Arguments.of(null, "null")
        );
    }
}
