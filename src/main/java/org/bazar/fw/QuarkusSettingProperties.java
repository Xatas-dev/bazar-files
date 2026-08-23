package org.bazar.fw;

import io.smallrye.config.ConfigMapping;
import org.bazar.app.api.ConfigProvider;
import org.bazar.domain.ValidationErrorCode;

import java.util.List;
import java.util.Map;

@ConfigMapping(prefix = "settings")
public interface QuarkusSettingProperties extends ConfigProvider {
    S3 s3();
    Scheduler scheduler();
    FileValidation fileValidation();

    interface S3 {
        Bucket bucket();
        int uploadUrlTtl();
        int downloadUrlTtl();
        String internalEndpoint();

        interface Bucket {
            String files();
        }
    }

    interface Scheduler {
        DeleteFiles deleteFiles();

        interface DeleteFiles {
            String cron();
            Integer batchSize();
        }
    }

    interface FileValidation {
        long maxFileSize();
        List<String> notAllowedExtensions();
        Map<ValidationErrorCode, String> errorMessages();
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

    @Override
    default long getMaxFileSize() {
        return fileValidation().maxFileSize();
    }

    @Override
    default List<String> getNotAllowedExtensions() {
        return fileValidation().notAllowedExtensions();
    }

    @Override
    default Map<ValidationErrorCode, String> getErrorMessages() {
        return fileValidation().errorMessages();
    }
}
