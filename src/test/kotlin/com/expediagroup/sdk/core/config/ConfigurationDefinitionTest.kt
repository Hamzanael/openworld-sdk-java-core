/*
 * Copyright (C) 2022 Expedia, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.expediagroup.sdk.core.config

import com.expediagroup.sdk.core.model.exception.ConfigurationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ConfigurationDefinitionTest {

    @Test
    fun `configuration definition lets you define configuration keys for a given type`() {

        val configurationDefinition = ConfigurationDefinition()
        val configurationKeyName = "test_configuration_key"
        val configurationKeyDocumentation = "This is a test configuration key of integer type"
        val configurationKeyType = ConfigurationKey.Type.INT
        val configurationKeyImportance = ConfigurationKey.Importance.HIGH

        val definition = configurationDefinition.define(
            name = configurationKeyName,
            documentation = configurationKeyDocumentation,
            type = configurationKeyType,
            importance = configurationKeyImportance
        )

        assertDoesNotThrow {
            val configurationKey = definition.get(configurationKeyName)
            assertEquals(configurationKey.name, configurationKeyName)
            assertEquals(configurationKey.documentation, configurationKeyDocumentation)
            assertEquals(configurationKey.type, configurationKeyType)
            assertEquals(configurationKey.importance, configurationKeyImportance)
            assertNull(configurationKey.defaultValue)
            assertNull(configurationKey.validator)
        }
    }

    @Test
    fun `successfully parses configurations when the configuration value matches the configuration type and validator`() {

        val configurationDefinition = ConfigurationDefinition()
        val configurationKeyName = "test_configuration_key"
        val configurationValue = 20
        val configurationKeyDocumentation = "This is a test configuration key of integer type"
        val configurationKeyType = ConfigurationKey.Type.INT
        val configurationKeyImportance = ConfigurationKey.Importance.HIGH
        val validator = object : ConfigurationKey.Validator {
            override fun ensureValid(name: String, value: Any): Any {
                return value
            }
        }

        configurationDefinition.define(
            name = configurationKeyName,
            documentation = configurationKeyDocumentation,
            type = configurationKeyType,
            importance = configurationKeyImportance,
            validator = validator
        )

        assertDoesNotThrow {
            val mappedConfigurations = configurationDefinition.parse(mapOf(configurationKeyName to configurationValue))
            assertNotNull(mappedConfigurations)
            assertTrue(mappedConfigurations[configurationKeyName] is Int)
            assertEquals(mappedConfigurations[configurationKeyName], configurationValue)
        }
    }

    @Test
    fun `successfully parses configurations of type list when the configuration value is empty`() {

        val configurationDefinition = ConfigurationDefinition()
        val configurationKeyName = "test_configuration_key"
        val configurationKeyDocumentation = "This is a test configuration key of list type"
        val configurationKeyType = ConfigurationKey.Type.LIST
        val configurationKeyImportance = ConfigurationKey.Importance.HIGH

        configurationDefinition.define(
            name = configurationKeyName,
            documentation = configurationKeyDocumentation,
            type = configurationKeyType,
            importance = configurationKeyImportance
        )

        assertDoesNotThrow {
            val mappedConfigurations = configurationDefinition.parse(mapOf(configurationKeyName to ""))
            assertNotNull(mappedConfigurations)
            assertTrue(mappedConfigurations[configurationKeyName] is List<*>)
            assertEquals(mappedConfigurations[configurationKeyName], emptyList<String>())
        }
    }

    @Test
    fun `successfully parses configurations of type int when the configuration value is a string-integer`() {

        val configurationDefinition = ConfigurationDefinition()
        val configurationKeyName = "test_configuration_key"
        val configurationKeyDocumentation = "This is a test configuration key of Int type"
        val configurationKeyType = ConfigurationKey.Type.INT
        val configurationKeyImportance = ConfigurationKey.Importance.HIGH

        configurationDefinition.define(
            name = configurationKeyName,
            documentation = configurationKeyDocumentation,
            type = configurationKeyType,
            importance = configurationKeyImportance
        )

        assertDoesNotThrow {
            val mappedConfigurations = configurationDefinition.parse(mapOf(configurationKeyName to "25"))
            assertNotNull(mappedConfigurations)
            assertTrue(mappedConfigurations[configurationKeyName] is Int)
            assertEquals(mappedConfigurations[configurationKeyName], 25)
        }
    }
    @Test
    fun `successfully parses configurations of type double when the configuration value is an integer`() {

        val configurationDefinition = ConfigurationDefinition()
        val configurationKeyName = "test_configuration_key"
        val configurationKeyDocumentation = "This is a test configuration key of Double type"
        val configurationKeyType = ConfigurationKey.Type.DOUBLE
        val configurationKeyImportance = ConfigurationKey.Importance.HIGH

        configurationDefinition.define(
            name = configurationKeyName,
            documentation = configurationKeyDocumentation,
            type = configurationKeyType,
            importance = configurationKeyImportance
        )

        assertDoesNotThrow {
            val mappedConfigurations = configurationDefinition.parse(mapOf(configurationKeyName to 25))
            assertNotNull(mappedConfigurations)
            assertTrue(mappedConfigurations[configurationKeyName] is Double)
            assertEquals(mappedConfigurations[configurationKeyName], 25.0)
        }
    }

    @Test
    fun `successfully parses configurations of type double when the configuration value is a string-integer`() {

        val configurationDefinition = ConfigurationDefinition()
        val configurationKeyName = "test_configuration_key"
        val configurationKeyDocumentation = "This is a test configuration key of Double type"
        val configurationKeyType = ConfigurationKey.Type.DOUBLE
        val configurationKeyImportance = ConfigurationKey.Importance.HIGH

        configurationDefinition.define(
            name = configurationKeyName,
            documentation = configurationKeyDocumentation,
            type = configurationKeyType,
            importance = configurationKeyImportance
        )

        assertDoesNotThrow {
            val mappedConfigurations = configurationDefinition.parse(mapOf(configurationKeyName to "25.5"))
            assertNotNull(mappedConfigurations)
            assertTrue(mappedConfigurations[configurationKeyName] is Double)
            assertEquals(mappedConfigurations[configurationKeyName], 25.5)
        }
    }

    @Test
    fun `successfully parses configurations of type password`() {

        val configurationDefinition = ConfigurationDefinition()
        val configurationKeyName = "test_configuration_key"
        val configurationKeyDocumentation = "This is a test configuration key of Double type"
        val configurationKeyType = ConfigurationKey.Type.PASSWORD
        val configurationKeyImportance = ConfigurationKey.Importance.HIGH
        val configurationValue = ConfigurationKey.Password("secret")

        configurationDefinition.define(
            name = configurationKeyName,
            documentation = configurationKeyDocumentation,
            type = configurationKeyType,
            importance = configurationKeyImportance
        )

        assertDoesNotThrow {
            val mappedConfigurations = configurationDefinition.parse(mapOf(configurationKeyName to configurationValue))
            assertNotNull(mappedConfigurations)
            assertTrue(mappedConfigurations[configurationKeyName] is ConfigurationKey.Password)
            assertEquals(mappedConfigurations[configurationKeyName], configurationValue)
        }
    }

    @Test
    fun `sets default value when configuration value is not present`() {

        val configurationDefinition = ConfigurationDefinition()
        val configurationKeyName = "test_configuration_key"
        val configurationKeyDocumentation = "This is a test configuration key of integer type"
        val configurationDefaultValue = 25
        val configurationKeyType = ConfigurationKey.Type.INT
        val configurationKeyImportance = ConfigurationKey.Importance.HIGH
        val definition = configurationDefinition.define(
            name = configurationKeyName,
            documentation = configurationKeyDocumentation,
            type = configurationKeyType,
            importance = configurationKeyImportance,
            defaultValue = configurationDefaultValue
        )

        assertDoesNotThrow {
            val configuration = definition.parse(mapOf())
            assertTrue(configuration[configurationKeyName] is Int)
            assertEquals(configuration[configurationKeyName] as Int, configurationDefaultValue)
        }
    }

    @Test
    fun `throws an exception when same configuration is defined more than once`() {

        val configurationDefinition = ConfigurationDefinition()
        val configurationName = "test_configuration_key"
        val configurationDocumentation = "This is a test configuration key of integer type"
        val configurationType = ConfigurationKey.Type.INT
        val configurationImportance = ConfigurationKey.Importance.HIGH
        configurationDefinition.define(
            name = configurationName,
            documentation = configurationDocumentation,
            type = configurationType,
            importance = configurationImportance
        )
        val throwable = assertThrows<ConfigurationException> {
            configurationDefinition.define(
                name = configurationName,
                documentation = configurationDocumentation,
                type = configurationType,
                importance = configurationImportance
            )
        }
        assertEquals(throwable.message, "Configuration $configurationName is defined twice")
    }

    @Test
    fun `throws an exception when configuration value doesn't match type for boolean`() {

        val configurationDefinition = ConfigurationDefinition()
        val booleanConfigurationName = "boolean_configuration"
        val configurationDocumentation = "This is a test configuration documentation"
        val configurationImportance = ConfigurationKey.Importance.HIGH
        configurationDefinition.define(
            name = booleanConfigurationName,
            documentation = configurationDocumentation,
            type = ConfigurationKey.Type.BOOLEAN,
            importance = configurationImportance
        )

        val throwableWhenInvalidString = assertThrows<ConfigurationException> {
            configurationDefinition.parse(mapOf(booleanConfigurationName to "invalid"))
        }
        assertEquals(
            throwableWhenInvalidString.message,
            "Expected value to be either true or false, name:$booleanConfigurationName, value:invalid"
        )

        val throwableWhenInvalidInt = assertThrows<ConfigurationException> {
            configurationDefinition.parse(mapOf(booleanConfigurationName to 213))
        }
        assertEquals(
            throwableWhenInvalidInt.message,
            "Expected value to be either true or false, name:$booleanConfigurationName, value:213"
        )
    }

    @Test
    fun `throws an exception when configuration value doesn't match type for password`() {

        val configurationDefinition = ConfigurationDefinition()
        val passwordConfigurationName = "password_configuration"
        val configurationDocumentation = "This is a test configuration documentation"
        val configurationImportance = ConfigurationKey.Importance.HIGH
        val props = mapOf(passwordConfigurationName to 123)
        configurationDefinition.define(
            name = passwordConfigurationName,
            documentation = configurationDocumentation,
            type = ConfigurationKey.Type.PASSWORD,
            importance = configurationImportance
        )

        val throwable = assertThrows<ConfigurationException> {
            configurationDefinition.parse(props)
        }
        assertEquals(
            throwable.message,
            "Expected value to be a string, but it was a java.lang.Integer, name:$passwordConfigurationName, value:123"
        )
    }

    @Test
    fun `throws an exception when configuration value doesn't match type for integer`() {

        val configurationDefinition = ConfigurationDefinition()
        val configurationName = "test_configuration"
        val configurationDocumentation = "This is a test configuration documentation"
        val configurationImportance = ConfigurationKey.Importance.HIGH
        val props = mapOf(configurationName to "invalid")
        configurationDefinition.define(
            name = configurationName,
            documentation = configurationDocumentation,
            type = ConfigurationKey.Type.INT,
            importance = configurationImportance
        )

        val throwableWhenInvalidString = assertThrows<ConfigurationException> {
            configurationDefinition.parse(props)
        }
        assertEquals(
            throwableWhenInvalidString.message,
            "Expected value to be a  32-bit integer, but it was a java.lang.String, name:$configurationName, value:invalid"
        )
        val throwableWhenInvalidBoolean = assertThrows<ConfigurationException> {
            configurationDefinition.parse(mapOf(configurationName to true))
        }
        assertEquals(
            throwableWhenInvalidBoolean.message,
            "Expected value to be a  32-bit integer, but it was a java.lang.Boolean, name:$configurationName, value:true"
        )
    }

    @Test
    fun `throws an exception when configuration value doesn't match type for double`() {

        val configurationDefinition = ConfigurationDefinition()
        val configurationName = "test_configuration"
        val configurationDocumentation = "This is a test configuration documentation"
        val configurationImportance = ConfigurationKey.Importance.HIGH
        configurationDefinition.define(
            name = configurationName,
            documentation = configurationDocumentation,
            type = ConfigurationKey.Type.DOUBLE,
            importance = configurationImportance
        )
        val throwableWhenInvalidString = assertThrows<ConfigurationException> {
            configurationDefinition.parse(mapOf(configurationName to "invalid"))
        }
        assertEquals(
            throwableWhenInvalidString.message,
            "Expected value to be a double, but it was a java.lang.String, name:$configurationName, value:invalid"
        )
        val throwableWhenInvalidBoolean = assertThrows<ConfigurationException> {
            configurationDefinition.parse(mapOf(configurationName to true))
        }
        assertEquals(
            throwableWhenInvalidBoolean.message,
            "Expected value to be a double, but it was a java.lang.Boolean, name:$configurationName, value:true"
        )
    }

    @Test
    fun `throws an exception when configuration value doesn't match type for string`() {

        val configurationDefinition = ConfigurationDefinition()
        val stringConfigurationName = "string_configuration"
        val configurationDocumentation = "This is a test configuration documentation"
        val configurationImportance = ConfigurationKey.Importance.HIGH
        val props = mapOf(stringConfigurationName to 123)
        configurationDefinition.define(
            name = stringConfigurationName,
            documentation = configurationDocumentation,
            type = ConfigurationKey.Type.STRING,
            importance = configurationImportance
        )

        val throwable = assertThrows<ConfigurationException> {
            configurationDefinition.parse(props)
        }
        assertEquals(
            throwable.message,
            "Expected value to be a string, but it was a java.lang.Integer, name:$stringConfigurationName, value:123"
        )
    }

    @Test
    fun `throws an exception when configuration value doesn't match type for list`() {

        val configurationDefinition = ConfigurationDefinition()
        val listConfigurationName = "list_configuration"
        val configurationDocumentation = "This is a test configuration documentation"
        val configurationImportance = ConfigurationKey.Importance.HIGH
        val props = mapOf(listConfigurationName to 123)
        configurationDefinition.define(
            name = listConfigurationName,
            documentation = configurationDocumentation,
            type = ConfigurationKey.Type.LIST,
            importance = configurationImportance
        )

        val throwable = assertThrows<ConfigurationException> {
            configurationDefinition.parse(props)
        }
        assertEquals(throwable.message, "Expected a comma separated list, name:$listConfigurationName, value:123")
    }

    @Test
    fun `throws an exception when configuration key is not defined`() {

        val configurationDefinition = ConfigurationDefinition()
        val listConfigurationName = "list_configuration"

        val throwable = assertThrows<ConfigurationException> {
            configurationDefinition.get(listConfigurationName)
        }
        assertEquals(throwable.message, "configuration key not defined, name:$listConfigurationName")
    }

    @Test
    fun `throws an exception when configuration value doesn't pass the custom validator`() {

        val configurationDefinition = ConfigurationDefinition()
        val stringConfigurationName = "string_configuration"
        val configurationDocumentation = "This is a test configuration documentation"
        val configurationImportance = ConfigurationKey.Importance.HIGH
        val props = mapOf(stringConfigurationName to "something")
        val validator = object : ConfigurationKey.Validator {
            override fun ensureValid(name: String, value: Any): Any {
                val regex = Regex("^[A-Z]*\$")
                if (regex.matches(value as String)) {
                    return value
                }
                throw ConfigurationException("value is not uppercase")
            }
        }
        configurationDefinition.define(
            name = stringConfigurationName,
            documentation = configurationDocumentation,
            type = ConfigurationKey.Type.STRING,
            importance = configurationImportance,
            validator = validator
        )

        val throwable = assertThrows<ConfigurationException> {
            configurationDefinition.parse(props)
        }
        assertEquals(throwable.message, "value is not uppercase")
    }
}
