package com.spike.spikemicrosvc;

import org.flywaydb.core.Flyway;

import static spark.Spark.get;

public class Main {

	public static void main(String[] args) {
        final Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:postgresql://localhost:5432/spikedb", "postgres", "postgres");
        flyway.migrate();
        get("/hello", (req, res) -> "Hello World");
	}
}
