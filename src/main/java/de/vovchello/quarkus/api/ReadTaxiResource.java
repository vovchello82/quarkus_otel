package de.vovchello.quarkus.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/taxi")
public class ReadTaxiResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllTaxi() {
        return "stored taxi";
    }
}