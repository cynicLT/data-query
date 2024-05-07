package org.cynic.data_query.domain.http.post;

import java.io.Serial;
import java.io.Serializable;

public record StoreItemHttp(
    String id,
    String title,
    String content,
    Long views,
    Long timestamp) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
