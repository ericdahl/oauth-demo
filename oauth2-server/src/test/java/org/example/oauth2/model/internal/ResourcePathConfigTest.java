package org.example.oauth2.model.internal;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ResourcePathConfigTest {

    private static final String PREFIX = "/go";

    @Test
    public void testSimpleMatch() throws Exception {
        assertThat(new ResourcePathConfig("/mypath", "/mytarget").matches(PREFIX, "/go/mypath"), is(true));
    }

    @Test
    public void testSimpleMismatch() throws Exception {
        assertThat(new ResourcePathConfig("/mypath", "/mytarget").matches(PREFIX, "/foo"), is(false));
    }

    @Ignore
    @Test
    public void testPatternMatch() throws Exception {
        assertThat(new ResourcePathConfig("/{username}/mypath", "/mytarget").matches(PREFIX, "/foo/mypath"), is(true));
    }
}