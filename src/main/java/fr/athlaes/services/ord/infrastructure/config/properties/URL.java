package fr.athlaes.services.ord.infrastructure.config.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class URL {
    private int port;
    private String host;
    private String protocol;
}
