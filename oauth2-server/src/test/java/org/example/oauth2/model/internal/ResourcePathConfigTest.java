package org.example.oauth2.model.internal;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ResourcePathConfigTest {

    private static final String PREFIX = "/go";

    @Test
    public void testSimpleMatch() throws Exception {
        assertThat(new ResourcePathConfig("/mypath", "/mytarget").matches(PREFIX, "/go/mypath"), is("/go/mypath"));
    }

    @Test
    public void testSimpleMismatch() throws Exception {
        assertThat(new ResourcePathConfig("/mypath", "/mytarget").matches(PREFIX, "/foo"), is(nullValue()));
    }

    @Test
    public void testPatternMatch() throws Exception {
        assertThat(new ResourcePathConfig("/{username}/mypath", "/mytarget").matches(PREFIX, "/go/foo/mypath"), is("/go/{username}/mypath"));
    }

    @Test
    public void testPatternMismatchExtraPart() throws Exception {
        assertThat(new ResourcePathConfig("/{username}/mypath/something", "/mytarget").matches(PREFIX, "/go/foo/mypath"), is(nullValue()));
    }

    @Test
    public void testPatternMismatch() throws Exception {
        assertThat(new ResourcePathConfig("/{username}/otherpath/", "/mytarget").matches(PREFIX, "/go/foo/mypath"), is(nullValue()));
    }
}