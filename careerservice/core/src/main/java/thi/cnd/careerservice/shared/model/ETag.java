package thi.cnd.careerservice.shared.model;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import thi.cnd.careerservice.exception.InvalidETagHeaderException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ETag {

    private final String text;
    private final long value;

    public static ETag fromValue(long value) {
        return new ETag(Long.toString(value), "N/A");
    }

    public static ETag fromIfMatchHeader(String value) {
        return new ETag(value, "IF-Match");
    }

    public static Optional<ETag> fromIfNoneMatchHeader(String value) {
        return value == null ? Optional.empty() : Optional.of(new ETag(value, "IF-None-Match"));
    }

    public static ETag weak(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid next excepted revision with value " + value);
        }

        return new ETag("W/\"%s\"".formatted(String.valueOf(value)), "N/A");
    }

    private ETag(String value, String headerName) {
        this.text = value;
        long parsedValue;

        try {
            parsedValue = Long.parseLong(value);
        } catch (NumberFormatException e1) {
            try {
                parsedValue = Long.parseLong(StringUtils.substringBetween(value, "\"", "\""));
            } catch (NumberFormatException e2) {
                throw new InvalidETagHeaderException(headerName);
            }
        }

        this.value = parsedValue;
    }

}
