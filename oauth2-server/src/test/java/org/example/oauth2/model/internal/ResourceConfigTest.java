package org.example.oauth2.model.internal;

import org.example.oauth2.TestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(TestApplication.class)
public class ResourceConfigTest {

    @Autowired
    private ResourceConfig config;

    @Test
    public void shouldLoadProperties() throws Exception {
        // TODO: remove this getter if possible
        assertThat(config.getResources(), hasSize(2));
    }

    @Test
    public void shouldFindByPath() throws Exception {
        assertThat(config.findTarget("mypath").get(), is("mytarget"));
    }

    @Test
    public void shouldReturnNullIfNoMatch() throws Exception {
        assertThat(config.findTarget("invalid").isPresent(), is(false));
    }
}