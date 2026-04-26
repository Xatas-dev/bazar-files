package org.bazar.app.impl.output;

import org.bazar.domain.File;

/**
 * Результат обработки загруженного файла
 * Содержит информацию о файле и флаг, нужно ли публиковать событие
 */
public record ProcessResult(
        File file,
        boolean shouldPublish
) {
    public static ProcessResult success(File file, boolean statusChanged) {
        return new ProcessResult(file, statusChanged);
    }

    public static ProcessResult fail(File file) {
        return new ProcessResult(file, file != null);
    }
}

