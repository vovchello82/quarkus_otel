package de.vovchello.quarkus.api;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.vovchello.quarkus.internal.db.api.ReadTaxi;
import de.vovchello.quarkus.internal.db.api.Taxi;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

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
    public void testGetTaxiEndpointOk() {
        Taxi[] taxis = given()
                .when().get("/taxi")
                .then()
                .statusCode(200)
                .extract().as(Taxi[].class);

        assertTrue(taxis.length == db.size());
        assertTrue(Arrays.stream(taxis).allMatch(t -> db.contains(t)), "not all elements from db");
    }

    @Test
    public void testGetTaxiByIdEndpointOk() {
        Mockito.when(readTaxi.getTaxiById(any())).thenReturn(db.stream().findAny());
        given()
                .when().get("/taxi/{id}", "X")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetTaxiByIdEndpointNotOk() {
        given()
                .when().get("/taxi/{id}", "abc")
                .then()
                .statusCode(204);
    }

}