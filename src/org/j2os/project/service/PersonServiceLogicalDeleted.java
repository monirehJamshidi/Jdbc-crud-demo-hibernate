package org.j2os.project.service;

import org.j2os.project.entity.Person;
import org.j2os.project.repository.PersonRepository;

import java.util.List;

public class PersonServiceLogicalDeleted {
    private final PersonRepository repository;

    public PersonServiceLogicalDeleted(PersonRepository repository){
        this.repository = repository;
    }

    public void save(Person person){
        repository.save(person);
    }

    public Person find(Long id){
        return repository.find(id);
    }

    public List<Person> findAll(){
        return repository.findAll();
    }

    public void delete(Long id) {
        Person p = repository.find(id);// فقط رکوردهای فعال را برمی‌گرداند (@Where)
        if (p == null) throw new RuntimeException("Record not found or already deleted");

        repository.softDelete(p);  // soft delete
    }

    public void hardDelete(Long id) {
        Person p = repository.find(id);
        repository.hardDelete(p); // delete واقعی
    }

    public void restore(Long id) {
        repository.restore(id);
    }

    public List<Person> findDeleted() {
        return repository.findDeleted();
    }
}