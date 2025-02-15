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
package com.expediagroup.sdk.core.plugin

internal interface Plugin<in C : PluginConfiguration> {
    /**
     * Install a plugin.
     */
    fun install(configurations: C)
}

/**
 * Provide an idiomatic scope to define plugins.
 */
internal fun plugins(block: () -> Unit) = block()

/**
 * Provides an idiomatic way of starting a plugin.
 */
internal fun <C : PluginConfiguration, P : Plugin<C>> use(plugin: P): P = plugin

/**
 * Provides an idiomatic way of configuring a plugin.
 */
internal fun <C : PluginConfiguration> Plugin<C>.with(configs: C): Unit = install(configs)
