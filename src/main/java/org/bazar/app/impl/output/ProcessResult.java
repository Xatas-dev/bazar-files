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
}

