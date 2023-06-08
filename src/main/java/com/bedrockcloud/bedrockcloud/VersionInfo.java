package com.bedrockcloud.bedrockcloud;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface VersionInfo {
    String name();
    
    String version();
    
    String identifier();
    
    String[] developers();
}
