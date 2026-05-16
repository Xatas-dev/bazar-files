package org.bazar.adapter.schedule;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bazar.app.api.DeleteMarkedFilesInbound;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class FilesDeletingScheduler {
    private final DeleteMarkedFilesInbound deleteMarkedFilesInbound;

    @Scheduled(every = "{settings.scheduler.delete-files.cron}")
    void deleteFiles() {
        log.info("Deleting files...");
        deleteMarkedFilesInbound.execute();
        log.info("Files deleted");
    }
}
