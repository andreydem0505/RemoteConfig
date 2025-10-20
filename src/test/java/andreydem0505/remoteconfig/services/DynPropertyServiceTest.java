package andreydem0505.remoteconfig.services;

import andreydem0505.remoteconfig.data.documents.DynProperty;
import andreydem0505.remoteconfig.data.documents.PropertyType;
import andreydem0505.remoteconfig.data.repositories.DynPropertyRepository;
import andreydem0505.remoteconfig.exceptions.DynPropertyAlreadyExistsException;
import andreydem0505.remoteconfig.exceptions.DynPropertyDataValidationException;
import andreydem0505.remoteconfig.exceptions.DynPropertyNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DynPropertyServiceTest {

    @Autowired
    private DynPropertyService dynPropertyService;

    @Autowired
    private DynPropertyRepository dynPropertyRepository;

    @BeforeEach
    void setUp() {
        dynPropertyRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        dynPropertyRepository.deleteAll();
    }

    // CUSTOM_PROPERTY tests
    private static Stream<Arguments> provideSimpleDataTypes() {
        return Stream.of(
                Arguments.of("john.doe", "theme", "dark-mode"),
                Arguments.of("jane.smith", "max_connections", 100),
                Arguments.of("robert.johnson", "pi_value", 3.14159),
                Arguments.of("alice.williams", "session_timeout", 1699876543210L),
                Arguments.of("michael.brown", "is_premium", true),
                Arguments.of("sarah.davis", "last_login", new Date(1729425000000L))
        );
    }

    @ParameterizedTest(name = "User {0} with property {1}")
    @MethodSource("provideSimpleDataTypes")
    void testCreateDynProperty_WhenCustomPropertyWithVariousTypes_ThenPropertyIsCreated(
            String username, String propertyName, Object data) {

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, data);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Property should be saved in repository");
        assertEquals(data, savedProperty.getData(), "Property value should be preserved");
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithNullData_ThenPropertyIsCreated() {
        String username = "emily.wilson";
        String propertyName = "optional_setting";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, null);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Property should be saved even with null data");
        assertNull(savedProperty.getData(), "Null data should be preserved");
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithListData_ThenPropertyIsCreated() {
        String username = "david.martinez";
        String propertyName = "favorite_colors";
        List<String> listData = Arrays.asList("red", "green", "blue");

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, listData);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Property with list data should be saved");
        assertEquals(listData, savedProperty.getData(), "List should be preserved with all elements");
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithMapData_ThenPropertyIsCreated() {
        String username = "jessica.garcia";
        String propertyName = "user_preferences";
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("language", "en");
        mapData.put("notifications", true);
        mapData.put("volume", 75);

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, mapData);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Property with map data should be saved");
        assertEquals(mapData, savedProperty.getData(), "Map should be preserved with all key-value pairs");
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithNestedMapData_ThenPropertyIsCreated() {
        String username = "christopher.lee";
        String propertyName = "app_config";
        Map<String, Object> nestedMapData = new HashMap<>();

        Map<String, Object> uiSettings = new HashMap<>();
        uiSettings.put("theme", "dark");
        uiSettings.put("fontSize", 14);

        Map<String, Object> privacySettings = new HashMap<>();
        privacySettings.put("analyticsEnabled", false);
        privacySettings.put("cookiesAccepted", true);

        nestedMapData.put("ui", uiSettings);
        nestedMapData.put("privacy", privacySettings);
        nestedMapData.put("version", "2.0");

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, nestedMapData);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Property with nested map should be saved");
        assertEquals(nestedMapData, savedProperty.getData(), "Nested map structure should be preserved");
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithComplexStructure_ThenPropertyIsCreated() {
        String username = "amanda.taylor";
        String propertyName = "dashboard_layout";
        Map<String, Object> complexData = new HashMap<>();

        List<Map<String, Object>> widgets = Arrays.asList(
                Map.of("id", 1, "type", "chart", "position", Map.of("x", 0, "y", 0)),
                Map.of("id", 2, "type", "table", "position", Map.of("x", 1, "y", 0)),
                Map.of("id", 3, "type", "text", "position", Map.of("x", 0, "y", 1))
        );

        complexData.put("widgets", widgets);
        complexData.put("columns", 2);
        complexData.put("created", new Date(1729414800000L));

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, complexData);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Property with complex nested structure should be saved");
        @SuppressWarnings("unchecked")
        Map<String, Object> savedData = (Map<String, Object>) savedProperty.getData();
        assertNotNull(savedData, "Complex nested structure should be preserved");
        assertEquals(2, savedData.get("columns"), "Columns value should be preserved");
        assertNotNull(savedData.get("widgets"), "Widgets list should be preserved");
        assertNotNull(savedData.get("created"), "Created date should be preserved");
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithEmptyList_ThenPropertyIsCreated() {
        String username = "daniel.anderson";
        String propertyName = "empty_tags";
        List<String> emptyList = List.of();

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, emptyList);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Property with empty list should be saved");
        assertTrue(((List<?>) savedProperty.getData()).isEmpty(), "Empty list should be preserved");
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithEmptyMap_ThenPropertyIsCreated() {
        String username = "elizabeth.thomas";
        String propertyName = "empty_metadata";
        Map<String, Object> emptyMap = new HashMap<>();

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, emptyMap);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Property with empty map should be saved");
        assertTrue(((Map<?, ?>) savedProperty.getData()).isEmpty(), "Empty map should be preserved");
    }

    // Property name validation tests
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
    void testCreateDynProperty_WhenValidPropertyName_ThenPropertyIsCreated(String propertyName) {
        String username = "user." + UUID.randomUUID();
        String data = "test_value";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, data);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Property with valid name should be saved");
        assertEquals(propertyName, savedProperty.getPropertyName(), "Property name should match");
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
    void testCreateDynProperty_WhenInvalidPropertyName_ThenThrowsValidationException(String invalidPropertyName) {
        String username = "test.user";
        String data = "test_value";

        DynPropertyDataValidationException exception = assertThrows(
                DynPropertyDataValidationException.class,
                () -> dynPropertyService.createDynProperty(username, invalidPropertyName,
                        PropertyType.CUSTOM_PROPERTY, data),
                "Should throw validation exception for invalid property name");

        assertTrue(exception.getMessage().contains(invalidPropertyName),
                "Exception message should mention the invalid property name");
    }

    @Test
    void testCreateDynProperty_WhenNullPropertyName_ThenThrowsValidationException() {
        String username = "null.name.user";

        DynPropertyDataValidationException exception = assertThrows(
                DynPropertyDataValidationException.class,
                () -> dynPropertyService.createDynProperty(username, null, PropertyType.CUSTOM_PROPERTY, "data"),
                "Should throw validation exception for null property name");

        assertTrue(exception.getMessage().contains("Invalid property name"),
                "Exception message should indicate invalid property name");
    }

    // BOOLEAN_FEATURE_FLAG tests
    @ParameterizedTest
    @CsvSource({
            "developer, enable_beta_features, true",
            "tester, dark_mode_enabled, false"
    })
    void testCreateDynProperty_WhenBooleanFlagWithValidValue_ThenPropertyIsCreated(
            String username, String propertyName, Boolean flagValue) {

        dynPropertyService.createDynProperty(username, propertyName,
                PropertyType.BOOLEAN_FEATURE_FLAG, flagValue);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Boolean flag should be saved in repository");
        assertEquals(flagValue, savedProperty.getData(), "Boolean value should match expected");
    }

    @ParameterizedTest
    @CsvSource({
            "matthew.jackson, broken_flag, not_a_boolean",
            "olivia.white, numeric_flag, 123",
            "emma.lopez, double_flag, 1.5"
    })
    void testCreateDynProperty_WhenBooleanFlagWithInvalidType_ThenThrowsValidationException(
            String username, String propertyName, String invalidData) {

        DynPropertyDataValidationException exception = assertThrows(
                DynPropertyDataValidationException.class,
                () -> dynPropertyService.createDynProperty(username, propertyName,
                        PropertyType.BOOLEAN_FEATURE_FLAG, invalidData),
                "Should throw validation exception for non-boolean data");

        assertTrue(exception.getMessage().contains("BOOLEAN_FEATURE_FLAG"),
                "Exception message should mention the property type");
    }

    @Test
    void testCreateDynProperty_WhenBooleanFlagWithNullData_ThenThrowsValidationException() {
        String username = "olivia.white";
        String propertyName = "null_flag";

        assertThrows(DynPropertyDataValidationException.class,
                () -> dynPropertyService.createDynProperty(username, propertyName,
                        PropertyType.BOOLEAN_FEATURE_FLAG, null),
                "Should throw validation exception for null boolean data");
    }

    // PERCENTAGE_FEATURE_FLAG tests
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 50, 99, 100})
    void testCreateDynProperty_WhenPercentageFlagWithValidValue_ThenPropertyIsCreated(Integer percentage) {
        String username = "user.percentage." + percentage;
        String propertyName = "feature_rollout";

        dynPropertyService.createDynProperty(username, propertyName,
                PropertyType.PERCENTAGE_FEATURE_FLAG, percentage);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Percentage flag should be saved");
        assertEquals(percentage, savedProperty.getData(), "Percentage should match expected");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, 101, 200, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void testCreateDynProperty_WhenPercentageFlagOutOfRange_ThenThrowsException(Integer invalidPercentage) {
        String username = "invalid.user." + UUID.randomUUID();
        String propertyName = "invalid_percentage";

        DynPropertyDataValidationException exception = assertThrows(
                DynPropertyDataValidationException.class,
                () -> dynPropertyService.createDynProperty(username, propertyName,
                        PropertyType.PERCENTAGE_FEATURE_FLAG, invalidPercentage),
                "Should throw validation exception for out of range percentage");

        assertTrue(exception.getMessage().contains("PERCENTAGE_FEATURE_FLAG"),
                "Exception message should mention the property type");
    }

    @ParameterizedTest
    @CsvSource({
            "william.harris, wrong_type, fifty",
            "sophia.martin, decimal_percentage, 50.5"
    })
    void testCreateDynProperty_WhenPercentageFlagWithInvalidType_ThenThrowsException(
            String username, String propertyName, String invalidData) {

        assertThrows(DynPropertyDataValidationException.class,
                () -> dynPropertyService.createDynProperty(username, propertyName,
                        PropertyType.PERCENTAGE_FEATURE_FLAG, invalidData),
                "Should throw validation exception for non-integer percentage");
    }

    @Test
    void testCreateDynProperty_WhenPercentageFlagWithNullData_ThenThrowsValidationException() {
        String username = "null.percentage.user";
        String propertyName = "null_percentage";

        assertThrows(DynPropertyDataValidationException.class,
                () -> dynPropertyService.createDynProperty(username, propertyName,
                        PropertyType.PERCENTAGE_FEATURE_FLAG, null),
                "Should throw validation exception for null percentage");
    }

    // Duplicate property tests
    @Test
    void testCreateDynProperty_WhenPropertyAlreadyExists_ThenThrowsAlreadyExistsException() {
        String username = "john.doe";
        String propertyName = "existing_property";
        String initialData = "initial_value";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, initialData);

        assertThrows(DynPropertyAlreadyExistsException.class,
                () -> dynPropertyService.createDynProperty(username, propertyName,
                        PropertyType.CUSTOM_PROPERTY, "new_value"),
                "Should throw already exists exception when creating duplicate property");

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertEquals(initialData, savedProperty.getData(),
                "Original property data should remain unchanged after failed duplicate attempt");
    }

    @Test
    void testCreateDynProperty_WhenSamePropertyDifferentUser_ThenBothPropertiesCreated() {
        String firstUsername = "james.thompson";
        String secondUsername = "isabella.moore";
        String sharedPropertyName = "common_setting";
        String firstData = "user_one_value";
        String secondData = "user_two_value";

        dynPropertyService.createDynProperty(firstUsername, sharedPropertyName,
                PropertyType.CUSTOM_PROPERTY, firstData);
        dynPropertyService.createDynProperty(secondUsername, sharedPropertyName,
                PropertyType.CUSTOM_PROPERTY, secondData);

        DynProperty firstProperty = dynPropertyRepository.findByUsernameAndPropertyName(
                firstUsername, sharedPropertyName);
        DynProperty secondProperty = dynPropertyRepository.findByUsernameAndPropertyName(
                secondUsername, sharedPropertyName);

        assertNotNull(firstProperty, "First user's property should exist");
        assertNotNull(secondProperty, "Second user's property should exist");
        assertEquals(firstData, firstProperty.getData(), "First user's data should be preserved");
        assertEquals(secondData, secondProperty.getData(), "Second user's data should be preserved");
    }

    @Test
    void testCreateDynProperty_WhenSameUserDifferentProperties_ThenBothPropertiesCreated() {
        String username = "benjamin.clark";
        String firstPropertyName = "setting_one";
        String secondPropertyName = "setting_two";
        String firstData = "value_one";
        String secondData = "value_two";

        dynPropertyService.createDynProperty(username, firstPropertyName,
                PropertyType.CUSTOM_PROPERTY, firstData);
        dynPropertyService.createDynProperty(username, secondPropertyName,
                PropertyType.CUSTOM_PROPERTY, secondData);

        DynProperty firstProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, firstPropertyName);
        DynProperty secondProperty = dynPropertyRepository.findByUsernameAndPropertyName(username,
                secondPropertyName);

        assertNotNull(firstProperty, "First property should exist");
        assertNotNull(secondProperty, "Second property should exist");
        assertEquals(firstData, firstProperty.getData(), "First property data should be preserved");
        assertEquals(secondData, secondProperty.getData(), "Second property data should be preserved");
    }

    // getDynPropertyData tests
    @Test
    void testGetDynPropertyData_WhenPropertyExists_ThenReturnsCorrectData() {
        String username = "data.retrieval.user";
        String propertyName = "user_theme";
        String expectedData = "dark_mode";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, expectedData);

        Object actualData = dynPropertyService.getDynPropertyData(username, propertyName);

        assertEquals(expectedData, actualData, "Retrieved data should match stored data");
    }

    @Test
    void testGetDynPropertyData_WhenPropertyDoesNotExist_ThenThrowsNotFoundException() {
        String username = "nonexistent.user";
        String propertyName = "nonexistent_property";

        DynPropertyNotFoundException exception = assertThrows(
                DynPropertyNotFoundException.class,
                () -> dynPropertyService.getDynPropertyData(username, propertyName),
                "Should throw not found exception when property does not exist");

        assertNotNull(exception, "Exception should be thrown");
    }

    @Test
    void testGetDynPropertyData_WhenBooleanFlag_ThenReturnsBoolean() {
        String username = "boolean.flag.user";
        String propertyName = "premium_features_enabled";
        Boolean expectedData = true;

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.BOOLEAN_FEATURE_FLAG, expectedData);

        Object actualData = dynPropertyService.getDynPropertyData(username, propertyName);

        assertEquals(expectedData, actualData, "Boolean flag value should be retrieved correctly");
    }

    @Test
    void testGetDynPropertyData_WhenPercentageFlag_ThenReturnsInteger() {
        String username = "percentage.flag.user";
        String propertyName = "feature_rollout_percentage";
        Integer expectedData = 75;

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.PERCENTAGE_FEATURE_FLAG,
                expectedData);

        Object actualData = dynPropertyService.getDynPropertyData(username, propertyName);

        assertEquals(expectedData, actualData, "Percentage flag value should be retrieved correctly");
    }

    @Test
    void testGetDynPropertyData_WhenComplexData_ThenReturnsCompleteStructure() {
        String username = "complex.data.user";
        String propertyName = "application_config";
        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("max_retries", 3);
        expectedData.put("timeout_seconds", 30);
        expectedData.put("endpoints", Arrays.asList("api.example.com", "backup.example.com"));

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, expectedData);

        Object actualData = dynPropertyService.getDynPropertyData(username, propertyName);

        assertEquals(expectedData, actualData, "Complex data structure should be retrieved correctly");
    }

    @Test
    void testGetDynPropertyData_WhenNullData_ThenReturnsNull() {
        String username = "null.data.user";
        String propertyName = "optional_config";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, null);

        Object actualData = dynPropertyService.getDynPropertyData(username, propertyName);

        assertNull(actualData, "Null data should be retrieved as null");
    }

    @Test
    void testGetDynPropertyData_WhenSamePropertyDifferentUser_ThenReturnsUserSpecificData() {
        String firstUsername = "user.one";
        String secondUsername = "user.two";
        String sharedPropertyName = "language_preference";
        String firstUserData = "english";
        String secondUserData = "spanish";

        dynPropertyService.createDynProperty(firstUsername, sharedPropertyName, PropertyType.CUSTOM_PROPERTY,
                firstUserData);
        dynPropertyService.createDynProperty(secondUsername, sharedPropertyName, PropertyType.CUSTOM_PROPERTY,
                secondUserData);

        Object firstUserActualData = dynPropertyService.getDynPropertyData(firstUsername, sharedPropertyName);
        Object secondUserActualData = dynPropertyService.getDynPropertyData(secondUsername, sharedPropertyName);

        assertEquals(firstUserData, firstUserActualData, "First user should get their own data");
        assertEquals(secondUserData, secondUserActualData, "Second user should get their own data");
        assertNotEquals(firstUserActualData, secondUserActualData, "Data should be user-specific");
    }

    @Test
    void testGetDynPropertyData_WhenMultiplePropertiesForUser_ThenReturnsCorrectProperty() {
        String username = "multi.property.user";
        String firstPropertyName = "theme";
        String secondPropertyName = "language";
        String firstData = "dark";
        String secondData = "french";

        dynPropertyService.createDynProperty(username, firstPropertyName, PropertyType.CUSTOM_PROPERTY, firstData);
        dynPropertyService.createDynProperty(username, secondPropertyName, PropertyType.CUSTOM_PROPERTY, secondData);

        Object firstActualData = dynPropertyService.getDynPropertyData(username, firstPropertyName);
        Object secondActualData = dynPropertyService.getDynPropertyData(username, secondPropertyName);

        assertEquals(firstData, firstActualData, "First property should return correct data");
        assertEquals(secondData, secondActualData, "Second property should return correct data");
    }
}
