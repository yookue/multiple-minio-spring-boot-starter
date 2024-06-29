/*
 * Copyright (c) 2020 Yookue Ltd. All rights reserved.
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

package com.yookue.springstarter.multipleminio.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.yookue.springstarter.multipleminio.property.MinioProperties;
import com.yookue.springstarter.multipleminio.util.MinioConfigurationUtils;
import io.minio.MinioClient;
import lombok.NonNull;


/**
 * Primary configuration for {@link io.minio.MinioClient}
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = SecondaryMinioAutoConfiguration.PROPERTIES_PREFIX, name = "host")
@ConditionalOnClass(value = MinioClient.class)
@AutoConfigureAfter(value = PrimaryMinioAutoConfiguration.class)
public class SecondaryMinioAutoConfiguration {
    public static final String PROPERTIES_PREFIX = "spring.multiple-minio.secondary";    // $NON-NLS-1$
    public static final String MINIO_PROPERTIES = "secondaryMinioProperties";    // $NON-NLS-1$
    public static final String MINIO_CLIENT = "secondaryMinioClient";    // $NON-NLS-1$

    @Bean(name = MINIO_PROPERTIES)
    @ConditionalOnMissingBean(name = MINIO_PROPERTIES)
    @ConfigurationProperties(prefix = PROPERTIES_PREFIX)
    public MinioProperties minioProperties() {
        return new MinioProperties();
    }

    @Bean(name = MINIO_CLIENT)
    @ConditionalOnBean(name = MINIO_PROPERTIES, value = MinioProperties.class)
    @ConditionalOnMissingBean(name = MINIO_CLIENT)
    public MinioClient minioClient(@Qualifier(value = MINIO_PROPERTIES) @NonNull MinioProperties properties) throws Exception {
        return MinioConfigurationUtils.minioClient(properties);
    }
}
