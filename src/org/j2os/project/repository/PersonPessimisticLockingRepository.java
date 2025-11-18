package org.j2os.project.repository;

import org.j2os.project.common.JPA;
import org.j2os.project.entity.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import java.util.HashMap;
import java.util.Map;

public class PersonPessimisticLockingRepository {

    //save
    public void save(Person person) {
        EntityManager entityManager = JPA.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            entityManager.persist(person);
            entityTransaction.commit();
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();

            entityManager.close();
        }
    }
    // ----------------------------
    //   1) قفل بدبینانه معمولی
    //      SELECT ... FOR UPDATE
    // ----------------------------
    public Person findWithLock(Long id){
        EntityManager entityManager = JPA.getInstance();
        try {
            return entityManager.find(Person.class, id, LockModeType.PESSIMISTIC_WRITE);
        } finally {
            entityManager.close();
        }
    }

    // به‌روزرسانی با قفل بدبینانه
    public void updateWithLock(Long id, String newName, String newCity){
        EntityManager entityManager = JPA.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();

            Person locked = entityManager.find(
                    Person.class,
                    id,
                    LockModeType.PESSIMISTIC_WRITE);
            locked.setName(newName);
            locked.setCity(newCity);

            entityTransaction.commit();
        } catch (Exception e){
            if (entityTransaction.isActive()) entityTransaction.rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    // ---------------------------------
    //   2) قفل بدبینانه NOWAIT
    //      SELECT ... FOR UPDATE NOWAIT
    // ---------------------------------

    public Person findWithNoWaitLock(Long id){
        EntityManager entityManager = JPA.getInstance();

        try {
            Map<String, Object> props = new HashMap<>();
            props.put("javax.persistence.lock.timeout",0);   // NOWAIT

            return entityManager.find(Person.class, id, LockModeType.PESSIMISTIC_WRITE,props);
        } finally {
            entityManager.close();
        }
    }

    public void updateWithNoWait(Long id, String newName, String newCity){
        EntityManager entityManager = JPA.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            Map<String , Object> props = new HashMap<>();
            props.put("javax.persistence.lock.timeout",0);

            Person person = entityManager.find(
                    Person.class,
                    id,
                    LockModeType.PESSIMISTIC_WRITE,
                    props
            );
            person.setName(newName);
            person.setCity(newCity);

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            System.out.println("❌ NOWAIT Conflict → رکورد قفل است!");
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
