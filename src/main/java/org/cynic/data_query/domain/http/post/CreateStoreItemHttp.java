package org.cynic.data_query.domain.http.post;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

public record CreateStoreItemHttp(
    @NotBlank String id,
    @NotBlank String title,
    @NotBlank String content,
    @Min(0) Long views,
    @Min(0) Long timestamp) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
