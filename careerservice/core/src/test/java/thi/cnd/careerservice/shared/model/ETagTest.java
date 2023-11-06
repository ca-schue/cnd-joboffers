package thi.cnd.careerservice.shared.model;

import org.junit.jupiter.api.Test;


import thi.cnd.careerservice.exception.InvalidETagHeaderException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ETagTest {

    @Test
    void returnsCorrectValuesForIfMatchHeader() {
        var eTag = ETag.fromIfMatchHeader("W\"5\"");
        assertEquals("W\"5\"", eTag.getText());
        assertEquals(5, eTag.getValue());
    }

    @Test
    void returnsCorrectValuesForIfNoneMatchHeader() {
        var eTagOptional = ETag.fromIfNoneMatchHeader("W\"5\"");
        assertTrue(eTagOptional.isPresent());

        var eTag = eTagOptional.get();
        assertEquals("W\"5\"", eTag.getText());
        assertEquals(5, eTag.getValue());
    }

    @Test
    void returnsEmptyIfNoneMatchHeaderIsEmpty() {
        var eTag = ETag.fromIfNoneMatchHeader(null);
        assertTrue(eTag.isEmpty());
    }

    @Test
    void weakETagsAreFormattedCorrectly() {
        var eTag = ETag.weak(20);
        assertEquals("W/\"20\"", eTag.getText());
        assertEquals(20, eTag.getValue());
    }

    @Test
    void weakETagsLowerThanZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ETag.weak(-5));
    }

    @Test
    void throwsExceptionIfValueIsMalformed() {
        assertThrows(InvalidETagHeaderException.class, () -> ETag.fromIfMatchHeader("invalid"));
    }

}
