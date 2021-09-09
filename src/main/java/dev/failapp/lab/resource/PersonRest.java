package dev.failapp.lab.resource;

import dev.failapp.lab.model.Person;
import dev.failapp.lab.service.PersonService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/")
public class PersonRest {

    @Inject
    private PersonService personService;

    @GET
    public Response index() {
        return Response.status(Response.Status.NOT_FOUND)
                .build();
    }

    @GET
    @Path("/api/v1/lab/persons")
    public List<Person> list(@QueryParam Integer page) {
        if (Optional.ofNullable(page).isEmpty()) page=1;
        return personService.fetchPersons(page);
    }

    @GET
    @Path("/api/v1/lab/persons/{documentId}")
    public Response findByDocumentId(@PathParam String documentId) {

        Optional<Person> person = personService.fetchPerson(documentId);
        if (person.isPresent())
            return Response.ok(person.get()).build();

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/api/v1/lab/persons")
    public Response save(Person person) {

        Optional<Person> _person = personService.storePerson(person);
        if (_person.isPresent())
            return Response.ok(_person.get()).build();

        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @DELETE
    @Path("/api/v1/lab/persons/{documentId}")
    public Response deleteByDocumentId(@PathParam String documentId) {

        boolean delete = personService.deletePerson(documentId);
        if (delete)
            return Response.ok().build();

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/api/v1/lab/persons/count")
    public long count() {
        return personService.countPersons();
    }


}
