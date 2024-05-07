package org.cynic.data_query.domain;


import java.io.Serial;
import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;

public final class ApplicationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String code;
    private final transient Map<String, Object> values;


    public ApplicationException(String code, Throwable e, Map<String, Object> values) {
        super(code, e);

        this.code = code;
        this.values = ObjectUtils.cloneIfPossible(values);
    }


    public ApplicationException(String code, Map<String, Object> values) {
        this(code, null, values);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    public ApplicationException(String code, Throwable e, Map.Entry<String, ?>... values) {
        this(code,
            e,
            Arrays.stream(values)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    public ApplicationException(final String code, Map.Entry<String, ?>... values) {
        this(code,
            Arrays.stream(values)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    public String getCode() {
        return code;
    }

    public Map<String, Object> getValues() {
        return ObjectUtils.cloneIfPossible(values);
    }

    @Override
    public String getMessage() {
        return new StringJoiner(",")
            .merge(new StringJoiner("=").add("code").add(code))
            .merge(new StringJoiner("=").add("values").add(values.toString()))
            .toString();
    }
}
