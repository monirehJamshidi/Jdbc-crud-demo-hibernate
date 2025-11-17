package org.j2os.project.repository;

import org.j2os.project.common.JPA;
import org.j2os.project.entity.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class PersonRepository {

    public void save(Person person){
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

    public Person find(Long id){
        EntityManager entityManager = JPA.getInstance();
        try {
            return entityManager.find(Person.class, id);
        } finally {
            entityManager.close();
        }
    }

    public List<Person> findAll(){
        EntityManager entityManager = JPA.getInstance();
        try {
            return entityManager.createQuery("select o from person o", Person.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<Person> findDeleted(){
        EntityManager entityManager = JPA.getInstance();
        try {
            return entityManager.createQuery("select o from person o where o.deleted = true ", Person.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // delete منطقی (Soft Delete)
    public void softDelete(Person person){
        EntityManager entityManager = JPA.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();

            int result = entityManager.createQuery(
                            "UPDATE person p SET p.deleted = true, p.recordVersion = p.recordVersion + 1 " +
                                    "WHERE p.id = :id AND p.recordVersion = :version"
                    )
                    .setParameter("id", person.getId())
                    .setParameter("version", person.getRecordVersion())
                    .executeUpdate();

//            Person managed = entityManager.merge(person);
//            entityManager.remove(managed);// ← Hibernate آن را تبدیل می‌کند به UPDATE
            entityTransaction.commit();
            if (result == 0) {
                throw new RuntimeException("Soft Delete failed (Optimistic Lock conflict)");
            }
        } catch (Exception e) {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            throw e;
        } finally {

            entityManager.close();
        }
    }

    // delete واقعی (برای مدیریت خاص)
    public void hardDelete(Person person){
        EntityManager entityManager = JPA.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            Person managed = entityManager.merge(person);
            entityManager.createQuery("delete from person p where p.id = :id")
                    .setParameter("id", person.getId())
                    .executeUpdate();
            entityTransaction.commit();
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    // restore (بازگردانی)
    public void restore(Long id){
        EntityManager entityManager = JPA.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            entityManager.createQuery("UPDATE person p SET p.deleted = false, p.recordVersion = p.recordVersion + 1 " +
                            "WHERE p.id = :id"
                    )
                    .setParameter("id", id)
                    .executeUpdate();
            entityTransaction.commit();
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }
}
