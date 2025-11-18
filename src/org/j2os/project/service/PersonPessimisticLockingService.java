package org.j2os.project.service;

import org.j2os.project.entity.Person;
import org.j2os.project.repository.PersonPessimisticLockingRepository;

public class PersonPessimisticLockingService {
    private final PersonPessimisticLockingRepository repository;

    public PersonPessimisticLockingService(PersonPessimisticLockingRepository repository){
        this.repository = repository;
    }

    public void save(Person person){
        repository.save(person);
    }
    public Person findWithLock(Long id){
        return repository.findWithLock(id);
    }

    public void updateWithLock(Long id, String newName, String newCity){
        repository.updateWithLock(id, newName, newCity);
    }

    public Person findWithNoWaitLock(Long id){
        return repository.findWithNoWaitLock(id);
    }

    public void updateWithNoWait(Long id, String newName, String newCity){
        repository.updateWithNoWait(id, newName, newCity);
    }

}
