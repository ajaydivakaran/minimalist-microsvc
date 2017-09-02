package com.spike.spikemicrosvc;

import com.google.gson.Gson;
import com.spike.spikemicrosvc.factory.module.PostGresModule;
import dagger.Component;
import org.flywaydb.core.Flyway;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.util.StringColumnMapper;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;

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
        new Main().run();
    }

    public void run() {
        Handle h = DaggerMain_App.create().provideDBI().open();
        final HashMap<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("a", "b");
        final Gson gson = new Gson();
        final String json = gson.toJson(map);
        h.execute("insert into USERS (first_name, context) values (?, to_json(?::json))", "Brian", json);

        String context = h.createQuery("select context from USERS where id = :id")
                .bind("id", 1)
                .map(StringColumnMapper.INSTANCE)
                .first();

        System.out.println(gson.fromJson(context, HashMap.class));

        h.close();
    }
}
