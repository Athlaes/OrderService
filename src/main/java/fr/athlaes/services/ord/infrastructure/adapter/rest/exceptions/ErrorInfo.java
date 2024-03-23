package fr.athlaes.services.ord.infrastructure.adapter.rest.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo {
    private String path;
    private String method;
    @JsonProperty("error")
    private String message;
    @JsonProperty("status")
    private int statusCode;
}
