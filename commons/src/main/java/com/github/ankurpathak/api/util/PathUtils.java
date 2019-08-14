package com.github.ankurpathak.api.util;

import org.apache.commons.io.FileUtils;

import java.nio.file.Path;
import java.util.UUID;

public class PathUtils {

    public static Path createTempPath(String suffix, String prefix){
        return Path.of(FileUtils.getTempDirectoryPath(), String.format("%s%s%s", suffix, UUID.randomUUID().toString().replaceAll("-", ""),prefix));
    }
}
