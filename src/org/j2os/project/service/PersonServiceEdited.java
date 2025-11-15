package org.j2os.project.service;

import org.j2os.project.common.JPA;
import org.j2os.project.entity.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;

public class PersonServiceEdited {
    private static final int MAX_RETRIES = 3;

    public static void save(Person person){
        EntityManager entityManager = JPA.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            entityManager.persist(person);
            entityTransaction.commit();
        } catch (Exception e){
            if (entityTransaction.isActive()) entityTransaction.rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public static void update(Person person){
        int attempts = 0;

        while (attempts < MAX_RETRIES){
            attempts++;

            EntityManager entityManager = JPA.getInstance();
            EntityTransaction entityTransaction = entityManager.getTransaction();

            try {
                entityTransaction.begin();

                // **دوباره‌خوانی آخرین نسخه از DB**
                Person managed = entityManager.find(Person.class, person.getId(), LockModeType.OPTIMISTIC);

                // اعمال تغییرات
                managed.setName(person.getName());
                managed.setCity(person.getCity());

                entityTransaction.commit();
                entityManager.close();

                System.out.println("Update successful on attempt " + attempts);
                return; // پایان موفقیت‌آمیز
            } catch (OptimisticLockException ex){
                System.out.println("⚠️ OptimisticLockException → retrying (" + attempts + ")");

                if (entityTransaction.isActive()) entityTransaction.rollback();
                entityManager.close();

                if (attempts >= MAX_RETRIES) {
                    throw new RuntimeException("Update failed after retries", ex);
                }

                // ادامه حلقه → دوباره تلاش کن
            }  catch (Exception e) {
                if (entityTransaction.isActive()) entityTransaction.rollback();
                entityManager.close();
                throw e;
            }
        }
    }

    public static Person find(Long id){
        EntityManager entityManager = JPA.getInstance();
        Person person = entityManager.find(Person.class, id);
        entityManager.close();
        return person;
    }

    public static void delete(Person person){
        EntityManager entityManager = JPA.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            Person finded = entityManager.find(Person.class, person.getId());
            entityManager.remove(finded);
            entityTransaction.commit();
        } catch (Exception e){
            if (entityTransaction.isActive()) entityTransaction.rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
