package dev.failapp.lab.repository;

import dev.failapp.lab.model.Person;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

    public Optional<Person> findByDocumentId(String documentId) {
        return find("documentId", documentId).firstResultOptional();
    }

}
