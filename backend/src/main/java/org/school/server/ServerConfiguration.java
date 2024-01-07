package org.school.server;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.Getter;

public class ServerConfiguration extends Configuration {
    @JsonProperty
    @Getter
    private String jwtSecretKey;
}
