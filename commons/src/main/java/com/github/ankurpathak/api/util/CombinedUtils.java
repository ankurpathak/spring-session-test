package com.github.ankurpathak.api.util;

import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CombinedUtils {
    public static Map<String, String> toMap(MultiValuedMap<String, String> multiMap){
        if(MultiMapUtils.isEmpty(multiMap))
            return Collections.emptyMap();
        Map<String, Collection<String>> map = multiMap.asMap();
        return map.keySet().stream().filter( key -> map.get(key).isEmpty()).collect(Collectors.toMap(Function.identity(), key -> map.get(key).stream().findFirst().orElse("")));
    }
}
