package dev.failapp.lab.service;

import dev.failapp.lab.model.Person;
import dev.failapp.lab.repository.PersonRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PersonService {

    private static final Logger log = Logger.getLogger(PersonService.class);

    @Inject
    private PersonRepository personRepository;

    public long countPersons() {
        return personRepository.count();
    }

    public List<Person> fetchPersons(int page) {
        int perPage = 50;
        page = page -1;
        PanacheQuery<Person> persons = personRepository.findAll(Sort.ascending("id"));
        persons.page(Page.ofSize(perPage));
        return persons.page( Page.of(page, perPage) ).list();
    }

    public Optional<Person> fetchPerson(String documentId) {
        if (Optional.ofNullable(documentId).isEmpty()) return Optional.empty();
        return personRepository.findByDocumentId(documentId.strip());
    }

    @Transactional
    public Optional<Person> storePerson(Person person) {

        if (!this.validateData(person)) return Optional.empty();
        String documentId = person.getDocumentId().strip();
        Optional<Person> _person = personRepository.findByDocumentId(documentId);
        log.infof("[x] save entity: %s", person);
        if (_person.isPresent()) {
            _person.get().setFirstName( person.getFirstName() );
            _person.get().setLastName( person.getLastName() );
            _person.get().setAge( person.getAge() );
            personRepository.persist(_person.get());
            return _person;
        } else {
            personRepository.persist(person);
            return personRepository.findByDocumentId(documentId);
        }

    }

    @Transactional
    public boolean deletePerson(String documentId) {

        if (Optional.ofNullable(documentId).isEmpty()) return false;

        Optional<Person> person = personRepository.findByDocumentId(documentId.strip());
        if (person.isPresent()) {
            log.infof("[x] delete entity with documentId: %s", documentId);
            return personRepository.deleteById(person.get().getId());
        }
        return false;
    }

    private boolean validateData(Person person) {
        if (Optional.ofNullable(person).isEmpty())
            return false;
        if (Optional.ofNullable(person.getDocumentId()).isEmpty())
            return false;
        if (Optional.ofNullable(person.getFirstName()).isEmpty())
            return false;
        if (Optional.ofNullable(person.getLastName()).isEmpty())
            return false;
        if (Optional.ofNullable(person.getAge()).isEmpty())
            return false;

        return true;
    }

}
