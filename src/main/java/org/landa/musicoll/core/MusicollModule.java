package org.landa.musicoll.core;

import java.io.File;

import com.avaje.ebean.EbeanServer;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class MusicollModule extends AbstractModule {

    private final File basePath;

    public MusicollModule(final File basePath) {
        this.basePath = basePath;

    }

    @Override
    protected void configure() {

        bind(EbeanServer.class).toProvider(EbeanServerProvider.class);

        bind(File.class).annotatedWith(Names.named("basePath")).toInstance(basePath);
    }
}
