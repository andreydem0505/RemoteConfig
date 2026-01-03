package andreydem0505.remoteconfig.services;

import andreydem0505.remoteconfig.data.documents.DynProperty;
import andreydem0505.remoteconfig.data.documents.PropertyType;
import andreydem0505.remoteconfig.data.repositories.DynPropertyRepository;
import andreydem0505.remoteconfig.exceptions.DynPropertyAlreadyExistsException;
import andreydem0505.remoteconfig.exceptions.DynPropertyNotFoundException;
import andreydem0505.remoteconfig.exceptions.DynPropertyTypeValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DynPropertyServiceTest extends TestBase {

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
    @Test
    void testCreateDynProperty_WhenCustomPropertyWithStringData_ThenPropertyIsCreated() {
        String username = "john.doe";
        String propertyName = "theme";
        String data = "dark-mode";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, data);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty, "Property '%s' for user '%s' should be saved in repository"
                .formatted(propertyName, username));
        assertEquals(data, savedProperty.getData());
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithIntegerData_ThenPropertyIsCreated() {
        String username = "jane.smith";
        String propertyName = "max_connections";
        Integer data = 100;

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, data);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertEquals(data, savedProperty.getData());
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithDoubleData_ThenPropertyIsCreated() {
        String username = "robert.johnson";
        String propertyName = "pi_value";
        Double data = 3.14159;

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, data);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertEquals(data, savedProperty.getData());
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithLongData_ThenPropertyIsCreated() {
        String username = "alice.williams";
        String propertyName = "session_timeout";
        Long data = 1699876543210L;

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, data);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertEquals(data, savedProperty.getData());
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithBooleanData_ThenPropertyIsCreated() {
        String username = "michael.brown";
        String propertyName = "is_premium";
        Boolean data = true;

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, data);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertEquals(data, savedProperty.getData());
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithDateData_ThenPropertyIsCreated() {
        String username = "sarah.davis";
        String propertyName = "last_login";
        Date data = new Date(1729425000000L);

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, data);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertEquals(data, savedProperty.getData());
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithNullData_ThenPropertyIsCreated() {
        String username = "emily.wilson";
        String propertyName = "optional_setting";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, null);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertNull(savedProperty.getData());
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithListData_ThenPropertyIsCreated() {
        String username = "david.martinez";
        String propertyName = "favorite_colors";
        List<String> listData = Arrays.asList("red", "green", "blue");

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, listData);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertEquals(listData, savedProperty.getData());
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
        assertNotNull(savedProperty);
        assertEquals(mapData, savedProperty.getData());
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
        assertNotNull(savedProperty);
        assertEquals(nestedMapData, savedProperty.getData());
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
        assertNotNull(savedProperty);
        @SuppressWarnings("unchecked")
        Map<String, Object> savedData = (Map<String, Object>) savedProperty.getData();
        assertNotNull(savedData);
        assertEquals(2, savedData.get("columns"));
        assertNotNull(savedData.get("widgets"));
        assertNotNull(savedData.get("created"));
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithEmptyList_ThenPropertyIsCreated() {
        String username = "daniel.anderson";
        String propertyName = "empty_tags";
        List<String> emptyList = List.of();

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, emptyList);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertTrue(((List<?>) savedProperty.getData()).isEmpty());
    }

    @Test
    void testCreateDynProperty_WhenCustomPropertyWithEmptyMap_ThenPropertyIsCreated() {
        String username = "elizabeth.thomas";
        String propertyName = "empty_metadata";
        Map<String, Object> emptyMap = new HashMap<>();

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, emptyMap);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertTrue(((Map<?, ?>) savedProperty.getData()).isEmpty());
    }

    // BOOLEAN_FEATURE_FLAG tests
    @ParameterizedTest(name = "User ''{0}'' creates boolean flag with value {1}")
    @MethodSource("provideBooleanFlagValues")
    void testCreateDynProperty_WhenBooleanFlag_ThenPropertyIsCreated(String username, Boolean flagValue) {
        String propertyName = "enable_feature";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.BOOLEAN_FEATURE_FLAG, flagValue);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertEquals(flagValue, savedProperty.getData());
    }

    private static Stream<Arguments> provideBooleanFlagValues() {
        return Stream.of(
                Arguments.of("senior.developer", true),
                Arguments.of("junior.tester", false)
        );
    }

    // PERCENTAGE_FEATURE_FLAG tests
    @ParameterizedTest(name = "User ''{0}'' creates percentage flag with value {1}%")
    @MethodSource("providePercentageFlagValues")
    void testCreateDynProperty_WhenPercentageFlag_ThenPropertyIsCreated(String username, Integer percentage) {
        String propertyName = "feature_rollout";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.PERCENTAGE_FEATURE_FLAG, percentage);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertEquals(percentage, savedProperty.getData());
    }

    private static Stream<Arguments> providePercentageFlagValues() {
        return Stream.of(
                Arguments.of("marketing.manager", 0),
                Arguments.of("product.owner", 50),
                Arguments.of("release.engineer", 100)
        );
    }

    // EQUALITY_FEATURE_FLAG tests
    @ParameterizedTest(name = "User ''{0}'' creates equality flag with {2} value: {1}")
    @MethodSource("provideEqualityFlagValues")
    void testCreateDynProperty_WhenEqualityFlag_ThenPropertyIsCreated(
            String username, Object expectedValue, String valueType) {
        String propertyName = "feature_for_specific_value";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.EQUALITY_FEATURE_FLAG, expectedValue);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertNotNull(savedProperty);
        assertEquals(expectedValue, savedProperty.getData());
    }

    private static Stream<Arguments> provideEqualityFlagValues() {
        return Stream.of(
                Arguments.of("account.manager", "premium", "string"),
                Arguments.of("api.developer", 42, "integer"),
                Arguments.of("data.analyst", 1000000L, "long"),
                Arguments.of("finance.controller", 99.99, "double")
        );
    }

    // UNIT_IN_LIST_FEATURE_FLAG tests
    @ParameterizedTest(name = "Creates unit in list flag with {1} list containing {2} elements")
    @MethodSource("provideUnitInListFlagValues")
    void testCreateDynProperty_WhenUnitInListFlag_ThenPropertyIsCreated(
            List<?> allowedValues, String listType, int elementCount) {
        String propertyName = "feature_for_whitelisted_users";

        dynPropertyService.createDynProperty("security.admin", propertyName,
                PropertyType.UNIT_IN_LIST_FEATURE_FLAG, allowedValues);

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName("security.admin", propertyName);
        assertNotNull(savedProperty);
        assertEquals(allowedValues, savedProperty.getData());
    }

    private static Stream<Arguments> provideUnitInListFlagValues() {
        return Stream.of(
                Arguments.of(Arrays.asList("admin", "moderator", "vip"), "string", 3),
                Arguments.of(Arrays.asList(1, 2, 3, 5, 8), "integer", 5),
                Arguments.of(List.of(), "empty", 0)
        );
    }

    // checkHit tests for BOOLEAN_FEATURE_FLAG
    @ParameterizedTest(name = "Boolean flag value {0} returns {0} when checked")
    @ValueSource(booleans = {true, false})
    void testCheckHit_WhenBooleanFlag_ThenReturnsExpectedValue(boolean value) {
        String username = "feature.user";
        String propertyName = "new_ui_enabled";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.BOOLEAN_FEATURE_FLAG, value);

        boolean result = dynPropertyService.checkHit(username, propertyName, null);

        assertEquals(value, result);
    }

    // checkHit tests for PERCENTAGE_FEATURE_FLAG
    @ParameterizedTest(name = "Percentage flag with {0}% returns {1}")
    @MethodSource("providePercentageFlagCheckHitData")
    void testCheckHit_WhenPercentageFlag_ThenReturnsExpectedValue(
            Integer percentage, boolean expectedResult, String description) {
        String username = "rollout.manager";
        String propertyName = "gradual_feature_rollout";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.PERCENTAGE_FEATURE_FLAG, percentage);

        boolean result = dynPropertyService.checkHit(username, propertyName, null);

        assertEquals(expectedResult, result, description);
    }

    private static Stream<Arguments> providePercentageFlagCheckHitData() {
        return Stream.of(
                Arguments.of(0, false, "0% should always return false"),
                Arguments.of(100, true, "100% should always return true")
        );
    }

    // checkHit tests for EQUALITY_FEATURE_FLAG
    @Test
    void testCheckHit_WhenEqualityFlagMatchesContext_ThenReturnsTrue() {
        String username = "subscription.service";
        String propertyName = "premium_feature";
        String expectedValue = "premium";

        dynPropertyService.createDynProperty(username, propertyName,
                PropertyType.EQUALITY_FEATURE_FLAG, expectedValue);

        boolean result = dynPropertyService.checkHit(username, propertyName, "premium");

        assertTrue(result);
    }

    @Test
    void testCheckHit_WhenEqualityFlagDoesNotMatchContext_ThenReturnsFalse() {
        String username = "subscription.service";
        String propertyName = "premium_feature";
        String expectedValue = "premium";

        dynPropertyService.createDynProperty(username, propertyName,
                PropertyType.EQUALITY_FEATURE_FLAG, expectedValue);

        boolean result = dynPropertyService.checkHit(username, propertyName, "basic");

        assertFalse(result);
    }

    @Test
    void testCheckHit_WhenEqualityFlagWithNumericContext_ThenWorksCorrectly() {
        String username = "api.gateway";
        String propertyName = "version_specific_feature";
        Integer expectedVersion = 5;

        dynPropertyService.createDynProperty(username, propertyName,
                PropertyType.EQUALITY_FEATURE_FLAG, expectedVersion);

        boolean matchResult = dynPropertyService.checkHit(username, propertyName, 5);
        boolean noMatchResult = dynPropertyService.checkHit(username, propertyName, 3);

        assertTrue(matchResult);
        assertFalse(noMatchResult);
    }

    // checkHit tests for UNIT_IN_LIST_FEATURE_FLAG
    @ParameterizedTest(name = "{3}")
    @MethodSource("provideUnitInListTestData")
    void testCheckHit_WhenUnitInList_ThenReturnsExpectedValue(
            List<?> data,
            Object context,
            boolean expectedResult,
            String description
    ) {
        String username = "access.controller";
        String propertyName = "whitelist_feature";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.UNIT_IN_LIST_FEATURE_FLAG, data);

        boolean result = dynPropertyService.checkHit(username, propertyName, context);

        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> provideUnitInListTestData() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList("alice", "bob", "charlie"),
                        "bob",
                        true,
                        "Context in list returns true"
                ),
                Arguments.of(
                        Arrays.asList("alice", "bob", "charlie"),
                        "david",
                        false,
                        "Context not in list returns false"
                ),
                Arguments.of(
                        List.of(),
                        "anyone",
                        false,
                        "Empty list always returns false"
                ),
                Arguments.of(
                        Arrays.asList(100, 200, 300),
                        200,
                        true,
                        "Integer context in list returns true"
                ),
                Arguments.of(
                        Arrays.asList(100, 200, 300),
                        150,
                        false,
                        "Integer context not in list returns false"
                )
        );
    }

    // checkHit negative tests
    @Test
    void testCheckHit_WhenPropertyDoesNotExist_ThenThrowsNotFoundException() {
        String username = "nonexistent.user";
        String propertyName = "nonexistent_feature";

        assertThrows(
                DynPropertyNotFoundException.class,
                () -> dynPropertyService.checkHit(username, propertyName, "context")
        );
    }

    @Test
    void testCheckHit_WhenCustomProperty_ThenThrowsIncorrectTypeException() {
        String username = "config.user";
        String propertyName = "regular_setting";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, "value");

        assertThrows(
                DynPropertyTypeValidationException.class,
                () -> dynPropertyService.checkHit(username, propertyName, "context")
        );
    }

    // Duplicate property tests
    @Test
    void testCreateDynProperty_WhenPropertyAlreadyExists_ThenThrowsAlreadyExistsException() {
        String username = "john.doe";
        String propertyName = "existing_property";
        String initialData = "initial_value";

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, initialData);

        assertThrows(
                DynPropertyAlreadyExistsException.class,
                () -> dynPropertyService.createDynProperty(username, propertyName,
                        PropertyType.CUSTOM_PROPERTY, "new_value")
        );

        DynProperty savedProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        assertEquals(initialData, savedProperty.getData(),
                "Original property should remain unchanged after duplicate creation attempt");
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

        assertNotNull(firstProperty);
        assertNotNull(secondProperty);
    }

    @Test
    void testCreateDynProperty_WhenSameUserDifferentProperties_ThenBothPropertiesCreated() {
        String username = "benjamin.clark";
        String firstPropertyName = "setting_one";
        String secondPropertyName = "setting_two";

        dynPropertyService.createDynProperty(username, firstPropertyName,
                PropertyType.CUSTOM_PROPERTY, "value_one");
        dynPropertyService.createDynProperty(username, secondPropertyName,
                PropertyType.CUSTOM_PROPERTY, "value_two");

        DynProperty firstProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, firstPropertyName);
        DynProperty secondProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, secondPropertyName);

        assertNotNull(firstProperty);
        assertNotNull(secondProperty);
    }

    // getDynPropertyData tests
    @ParameterizedTest(name = "Retrieves {3} data type: {2}")
    @MethodSource("provideGetDynPropertyDataTestCases")
    void testGetDynPropertyData_WhenPropertyExists_ThenReturnsCorrectData(
            String username,
            String propertyName,
            Object expectedData,
            String dataType
    ) {

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.CUSTOM_PROPERTY, expectedData);

        Object actualData = dynPropertyService.getDynPropertyData(username, propertyName);

        assertEquals(expectedData, actualData);
    }

    private static Stream<Arguments> provideGetDynPropertyDataTestCases() {
        Map<String, Object> complexData = new HashMap<>();
        complexData.put("max_retries", 3);
        complexData.put("timeout_seconds", 30);
        complexData.put("endpoints", Arrays.asList("api.example.com", "backup.example.com"));

        return Stream.of(
                Arguments.of("data.retrieval.user", "user_theme", "dark_mode", "string"),
                Arguments.of("config.user", "max_connections", 100, "integer"),
                Arguments.of("settings.user", "timeout_value", 30.5, "double"),
                Arguments.of("feature.user", "is_enabled", true, "boolean"),
                Arguments.of("complex.data.user", "application_config", complexData, "complex map"),
                Arguments.of("null.data.user", "optional_config", null, "null")
        );
    }

    @Test
    void testGetDynPropertyData_WhenPropertyDoesNotExist_ThenThrowsNotFoundException() {
        String username = "nonexistent.user";
        String propertyName = "nonexistent_property";

        assertThrows(
                DynPropertyNotFoundException.class,
                () -> dynPropertyService.getDynPropertyData(username, propertyName)
        );
    }

    @Test
    void testGetDynPropertyData_WhenBooleanFlag_ThenReturnsBoolean() {
        String username = "flag.user";
        String propertyName = "premium_features_enabled";
        Boolean expectedData = true;

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.BOOLEAN_FEATURE_FLAG, expectedData);

        Object actualData = dynPropertyService.getDynPropertyData(username, propertyName);

        assertEquals(expectedData, actualData);
    }

    @Test
    void testGetDynPropertyData_WhenPercentageFlag_ThenReturnsInteger() {
        String username = "rollout.user";
        String propertyName = "feature_rollout_percentage";
        Integer expectedData = 75;

        dynPropertyService.createDynProperty(username, propertyName, PropertyType.PERCENTAGE_FEATURE_FLAG,
                expectedData);

        Object actualData = dynPropertyService.getDynPropertyData(username, propertyName);

        assertEquals(expectedData, actualData);
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

        assertEquals(firstUserData, firstUserActualData);
        assertEquals(secondUserData, secondUserActualData);
        assertNotEquals(firstUserActualData, secondUserActualData);
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

        assertEquals(firstData, firstActualData);
        assertEquals(secondData, secondActualData);
    }
}
