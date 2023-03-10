package de.vovchello.quarkus.api;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.vovchello.quarkus.internal.db.api.ReadTaxi;
import de.vovchello.quarkus.internal.db.api.Taxi;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@QuarkusTest
class TaxiFinderResourceTest {
    @InjectMock
    ReadTaxi readTaxi;

    private final static Set<Taxi> db = new HashSet<>(Arrays.asList(
            new Taxi("1", "tax1", true),
            new Taxi("2", "tax2", true)));

    @BeforeEach
    public void setup() {
        Mockito.when(readTaxi.getAllTaxies()).thenReturn(db);
    }

    @Test
    public void testGetTaxiEndpoint() {
        Taxi[] taxis = given()
                .when().get("/taxi")
                .then()
                .statusCode(200).extract().as(Taxi[].class);
        assertTrue(taxis.length == 2);
    }

}