package org.cynic.data_query;

import java.time.ZoneId;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class Constants {

    public static final Marker AUDIT_MARKER = MarkerFactory.getMarker("AUDIT");
    public static final ZoneId SYSTEM_ZONE_ID = ZoneId.systemDefault();

    public static final String QUOTE = "\"";

    private Constants() {
    }
}
