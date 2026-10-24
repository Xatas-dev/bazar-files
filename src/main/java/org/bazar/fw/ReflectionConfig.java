package org.bazar.fw;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bazar.app.impl.output.FileEventOut;

@RegisterForReflection(targets = {
        FileEventOut.class
})
public class ReflectionConfig {
}
