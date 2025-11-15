package org.j2os.project.service;

import org.j2os.project.common.JPA;
import org.j2os.project.entity.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.util.List;

public class PersonService {

    public static void save(Person person){
        EntityManager entityManager = JPA.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();
        entityManager.persist(person);
        entityTransaction.commit();
        entityManager.close();
    }

    public static void findOne(){
        EntityManager entityManager = JPA.getInstance();
        Person person1 = entityManager.find(Person.class, 1L);
        System.out.println(person1.getId() + " - " + person1.getName() + " - " + person1.getFamily());

        Person person2 = entityManager.find(Person.class, 2L);
        System.out.println(person2.getId() + " - " + person2.getName() + " - " + person2.getFamily());
    }

    public static void update(Person person){
        EntityManager entityManager = JPA.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        Person person_finded = entityManager.find(Person.class, person.getId());
        person_finded.setName(person.getName());
        person_finded.setFamily(person.getFamily());

        entityManager.persist(person_finded);
        entityTransaction.commit();
        entityManager.close();
    }

    public static void fidAll(){
        EntityManager entityManager = JPA.getInstance();
        Query query = entityManager.createQuery("select o from person o");
        List<Person> personList = query.getResultList();

        personList.forEach(person -> System.out.println(person.getId() + " - " + person.getName() + " - " + person.getFamily()));

        entityManager.close();
    }

    public static void findAllByName(String name){
        EntityManager entityManager = JPA.getInstance();
        Query query = entityManager.createQuery("select o from person o where o.name = '" + name + "'");
        List<Person> personList = query.getResultList();
        personList.forEach(person -> System.out.println(person.getId() + " - " + person.getName() + " - " + person.getFamily()));

        entityManager.close();
    }

    public static void findAllWithSpecialColumn(){
        EntityManager entityManager = JPA.getInstance();
        List<Tuple> tuples = entityManager.createQuery("select o.name as x , o.family as y from person  o ", Tuple.class).getResultList();
        tuples.stream().forEach(tuple -> System.out.println(tuple.get("x") + " - " + tuple.get("y")));

        entityManager.close();
    }

    public static void findAllWithSql(){
        EntityManager entityManager = JPA.getInstance();
        Query query = entityManager.createNativeQuery("select * from person", Person.class);
        List<Person> personList = query.getResultList();

        personList.forEach(person -> System.out.println(person.getId() + " - " + person.getName() + " - " + person.getFamily()));

        entityManager.close();
    }

    public static Person find(Long id){
        EntityManager entityManager = JPA.getInstance();
        Person person = entityManager.find(Person.class, id);

        entityManager.close();
        return person;
    }

    public static void updateWithMerge(Person person){
        EntityManager entityManager = JPA.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.merge(person);

        entityTransaction.commit();
        entityManager.close();
    }

 
}
