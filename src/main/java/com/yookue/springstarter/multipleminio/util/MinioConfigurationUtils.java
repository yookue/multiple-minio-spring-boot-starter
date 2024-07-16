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

package com.yookue.springstarter.multipleminio.util;


import javax.annotation.Nonnull;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationUtils;
import com.yookue.springstarter.multipleminio.property.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;


/**
 * Utilities for {@link io.minio.MinioClient}
 *
 * @author David Hsing
 * @see io.minio.MinioClient
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class MinioConfigurationUtils {
    @Nonnull
    public static MinioClient minioClient(@Nonnull MinioProperties properties) throws Exception {
        MinioClient.Builder clientBuilder = MinioClient.builder();
        clientBuilder.endpoint(properties.getHost(), ObjectUtils.defaultIfNull(properties.getPort(), 9000), BooleanUtils.isTrue(properties.getSecureHttp()));
        if (StringUtils.isNotBlank(properties.getAccessKey()) || StringUtils.isNotBlank(properties.getSecretKey())) {
            clientBuilder.credentials(properties.getAccessKey(), properties.getSecretKey());
        }
        if (StringUtils.isNotBlank(properties.getRegionName())) {
            clientBuilder.region(properties.getRegionName());
        }
        long connectTimeout = DurationUtils.isPositive(properties.getConnectTimeout()) ? properties.getConnectTimeout().toMillis() : 0L;
        long writeTimeout = DurationUtils.isPositive(properties.getWriteTimeout()) ? properties.getWriteTimeout().toMillis() : 0L;
        long readTimeout = DurationUtils.isPositive(properties.getReadTimeout()) ? properties.getReadTimeout().toMillis() : 0L;
        MinioClient minioClient = clientBuilder.build();
        minioClient.setTimeout(connectTimeout, writeTimeout, readTimeout);
        if (StringUtils.isNoneBlank(properties.getUserAgentName(), properties.getUserAgentVersion())) {
            minioClient.setAppInfo(properties.getUserAgentName(), properties.getUserAgentVersion());
        }
        if (BooleanUtils.isTrue(properties.getDualStackEnabled())) {
            minioClient.enableDualStackEndpoint();
        } else {
            minioClient.disableDualStackEndpoint();
        }
        if (BooleanUtils.isTrue(properties.getVirtualStyleEnabled())) {
            minioClient.enableVirtualStyleEndpoint();
        } else {
            minioClient.disableVirtualStyleEndpoint();
        }
        if (StringUtils.isNotBlank(properties.getBucketName()) && !minioClient.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build())) {
            MakeBucketArgs.Builder makeBuilder = MakeBucketArgs.builder().bucket(properties.getBucketName());
            if (StringUtils.isNotBlank(properties.getRegionName())) {
                makeBuilder.region(properties.getRegionName());
            }
            minioClient.makeBucket(makeBuilder.objectLock(true).build());
        }
        return minioClient;
    }
}
