/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("UnstableApiUsage")

plugins {
    id("dev.atick.library")
    id("dev.atick.dagger.hilt")
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "dev.atick.network"
}

dependencies {
    implementation(project(":core:android"))

    // ... OkHTTP
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    // ... Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
}