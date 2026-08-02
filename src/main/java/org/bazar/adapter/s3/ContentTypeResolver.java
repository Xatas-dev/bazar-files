package org.bazar.adapter.s3;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class ContentTypeResolver {
    private static final DefaultDetector DETECTOR = new DefaultDetector();

    public String getContentTypeByBytes(byte[] bytes) throws IOException, MimeTypeException {
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            MediaType mediaType = DETECTOR.detect(inputStream, new Metadata());

            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            return allTypes.forName(mediaType.toString()).getName();
        }
    }
}
