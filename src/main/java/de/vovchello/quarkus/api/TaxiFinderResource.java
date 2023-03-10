package de.vovchello.quarkus.api;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
}