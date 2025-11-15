package org.j2os.project;

import org.j2os.project.entity.Person;
import org.j2os.project.service.PersonService;
import org.j2os.project.service.PersonServiceEdited;

public class Main {
    public static void main(String[] args) {
//        Person person = Person.builder().name("Mina").family("Jamshidi").build();
//        PersonService.save(person);

//        PersonService.findOne();
//        Person person = Person.builder().personId(1).name("Mona").family("Jamshidi").build();
//        PersonService.update(person);

//        PersonService.fidAll();

//        PersonService.findAllByName("Mona");
//        PersonService.findAllWithSpecialColumn();
//        PersonService.findAllWithSql();

//        Person person = Person.builder().personId(1L).name("Mona").family("Jamshidi").recordVersion(1).build();
//        PersonService.updateWithMerge(person);

        //------------------------- test OptimisticLockException -------------------------
//        // ایجاد رکورد اولیه
//        Person person = new Person("Monireh", "Jamshidi", "Vienna");
//        PersonService.save(person);
//
//        // شبیه‌سازی دو کاربر همزمان
//        Person userA = PersonService.find(person.getId());
//        Person userB = PersonService.find(person.getId());
//
//        // هرکدام تغییر می‌دهند
//        userA.setCity("Graz");
//        userB.setName("Mona");
//
//        // یکی زودتر ذخیره می‌کند
//        PersonService.updateWithMerge(userB);
//
//        // دومی دیرتر ذخیره می‌کند → OptimisticLockException
//        try {
//            PersonService.updateWithMerge(userA);
//        } catch (Exception e){
//            System.out.println("⚠️ OptimisticLockException: " + e.getMessage());
//        }

        //------------------------- test OptimisticLockException -------------------------

        //--------------- Optimistic Locking → Retry → Fresh Read → Merge ---------------
        // ایجاد رکورد اولیه
        Person person = new Person("Monireh", "Jamshidi", "Wien");
        PersonServiceEdited.save(person);

        // شبیه‌سازی دو کاربر همزمان
        Person userA = PersonServiceEdited.find(person.getId());
        Person userB = PersonServiceEdited.find(person.getId());

        // هرکدام تغییر می‌دهند
        userA.setCity("Graz");
        userB.setName("Mona");

        PersonServiceEdited.update(userB);

        PersonServiceEdited.update(userA);



        //--------------- Optimistic Locking → Retry → Fresh Read → Merge ---------------
    }
}
