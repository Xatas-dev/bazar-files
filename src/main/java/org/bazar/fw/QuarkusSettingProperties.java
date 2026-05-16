package org.bazar.fw;

import io.smallrye.config.ConfigMapping;
import org.bazar.app.api.ConfigProvider;

@ConfigMapping(prefix = "settings")
public interface QuarkusSettingProperties extends ConfigProvider {
    S3 s3();
    Scheduler scheduler();

    interface S3 {
        Bucket bucket();
        int uploadUrlTtl();
        int downloadUrlTtl();
    }

    interface Scheduler {
        DeleteFiles deleteFiles();
    }

    interface Bucket {
        String files();
    }

    interface DeleteFiles {
        String cron();
        Integer batchSize();
    }

    @Override
    default String getFilesBucketName() {
        return s3().bucket().files();
    }

    @Override
    default int getUploadUrlTtl() {
        return s3().uploadUrlTtl();
    }

    @Override
    default int getDownloadUrlTtl() {
        return s3().downloadUrlTtl();
    }

    @Override
    default int getDeletingFilesBatchSize() {
        return scheduler().deleteFiles().batchSize();
    }
}
