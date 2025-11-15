package org.j2os.project.common;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPA {
    private final static EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("J2OS");
    private JPA(){}
    public static EntityManager getInstance(){
        return FACTORY.createEntityManager();
    }
}
