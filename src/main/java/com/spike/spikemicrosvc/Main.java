package com.spike.spikemicrosvc;

import com.spike.spikemicrosvc.factory.module.PostGresModule;
import dagger.Component;
import org.flywaydb.core.Flyway;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.util.StringColumnMapper;

import javax.inject.Singleton;
import java.io.IOException;

public class Main {

    @Singleton
    @Component(modules = { PostGresModule.class})
    public interface App {
        DBI provideDBI();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:postgresql://localhost:5432/devdb", "postgres", "postgres");
        flyway.migrate();
//        final PGPoolingDataSource pgPoolingDataSource = new PGPoolingDataSource();
//        PGSimpleDataSource source = new PGSimpleDataSource();
//        source.setUser("postgres");
//        source.setPassword("postgres");
//        source.setUrl("jdbc:postgresql://localhost:5432/devdb");
//        pgPoolingDataSource.initializeFrom(source);
//        DBI dbi = new DBI(pgPoolingDataSource);
        new Main().run();
    }

    public void run() {

        Handle h = DaggerMain_App.create().provideDBI().open();
        h.execute("insert into USERS (first_name) values (?)", "Brian");

        String name = h.createQuery("select first_name from USERS where id = :id")
                .bind("id", 1)
                .map(StringColumnMapper.INSTANCE)
                .first();

        System.out.println(name);

        h.close();
    }
}
