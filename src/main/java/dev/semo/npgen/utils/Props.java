package dev.semo.npgen.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Props {

    @Value("${target.uri}")
    private String uri;

    public String getUri() {
        return uri;
    }
}
