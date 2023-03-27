package de.vovchello.quarkus.api;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.vovchello.quarkus.internal.db.api.Taxi;
import de.vovchello.quarkus.internal.taxifinder.api.TaxiFinder;

@Path("/taxi")
public class TaxiFinderResource {

    private final TaxiFinder taxiFinder;

    public TaxiFinderResource(TaxiFinder taxiFinder) {
        this.taxiFinder = taxiFinder;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Taxi> getAllTaxi() {
        return taxiFinder.findAllTaxi();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaxiById(@PathParam("id") String id) {
        return taxiFinder.findTaxiById(id).map(t -> Response.ok(t).build()).orElse(Response.noContent().build());
    }

    @GET
    @Path("query")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaxiByName(@QueryParam("name") String name) {
        return taxiFinder.findTaxiByName(name).map(t -> Response.ok(t).build()).orElse(Response.noContent().build());
    }
}