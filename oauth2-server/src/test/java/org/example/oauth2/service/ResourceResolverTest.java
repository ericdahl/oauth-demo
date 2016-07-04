package org.example.oauth2.service;

import org.example.oauth2.model.internal.ResourceConfig;
import org.example.oauth2.model.internal.ResourcePathConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class ResourceResolverTest {

    private ResourceResolver resolver;

    @Before
    public void setUp() throws Exception {
        ResourceConfig mockResourceConfig = Mockito.mock(ResourceConfig.class);
        ResourcePathConfig mockResourcePathConfig = Mockito.mock(ResourcePathConfig.class);
        when(mockResourcePathConfig.getPath()).thenReturn("/{username}/todos");
        when(mockResourcePathConfig.getTarget()).thenReturn("http://localhost:8080/{username}/todos");

        when(mockResourceConfig.findTarget(eq("/go/myusername/todos"))).thenReturn(Optional.of(mockResourcePathConfig));

        resolver = new ResourceResolver(mockResourceConfig, "/go");
    }

    @Test
    public void shouldResolve() throws Exception {
        assertThat(resolver.resolve("/go/myusername/todos").getTarget(), is("http://localhost:8080/myusername/todos"));
    }
}