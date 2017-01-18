package com.ir.etsy.clusterizer;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.codehaus.jackson.annotate.JsonCreator;

/**
 *
 * @author Dimitar
 */
public enum ListingState {

    ACTIVE("active"),
    REMOVED("removed"),
    SOLD_OUT("sold_out"),
    EXPIRED("expired"),
    EDIT("edit"),
    DRAFT("draft"),
    CRATE("create"),
    PRIVATE("private"),
    UNAVAILABLE("unavailable");

    private static final Map<String, ListingState> FORMAT_MAP = Stream.of(ListingState.values()).collect(
            Collectors.toMap(s -> s.state, Function.identity()));

    private final String state;

    private ListingState(String state) {
        this.state = state;
    }

    @JsonCreator
    public static ListingState fromString(String string) {
        ListingState status = FORMAT_MAP.get(string);
        if (status == null) {
            throw new IllegalArgumentException(string + " has no corresponding value");
        }
        return status;
    }

    @Override
    public String toString() {
        return state;
    }

}
