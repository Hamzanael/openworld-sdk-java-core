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
package com.expediagroup.sdk.core.plugin.logging

import com.expediagroup.sdk.core.plugin.KtorPluginConfiguration
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger

internal data class LoggingConfiguration(
    override val httpClientConfiguration: HttpClientConfig<out HttpClientEngineConfig>,
    val logger: Logger = Logger.DEFAULT,
    val level: LogLevel = LogLevel.INFO
) : KtorPluginConfiguration(httpClientConfiguration) {
    companion object {
        fun from(httpClientConfig: HttpClientConfig<out HttpClientEngineConfig>) = LoggingConfiguration(httpClientConfig)
    }
}
