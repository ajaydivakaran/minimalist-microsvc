package com.spike.spikemicrosvc.factory.module;

import dagger.Module;
import dagger.Provides;
import org.postgresql.ds.PGPoolingDataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.skife.jdbi.v2.DBI;

import javax.inject.Singleton;
import java.io.IOException;

@Module
public class PostGresModule {

    @Provides
    @Singleton
    public DBI createDBI() {
        final PGPoolingDataSource pgPoolingDataSource = new PGPoolingDataSource();
        PGSimpleDataSource source = new PGSimpleDataSource();
        source.setUser("postgres");
        source.setPassword("postgres");
        source.setUrl("jdbc:postgresql://localhost:5432/devdb");
        try {
            pgPoolingDataSource.initializeFrom(source);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new DBI(pgPoolingDataSource);
    }

}
