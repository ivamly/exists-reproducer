package ru.ivamly.exists.reproducer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static jakarta.persistence.Persistence.createEntityManagerFactory;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExistsTest {

    private static final String QUERY = "select exists (select 1 from User u where u.role.id = :id)";

    private EntityManagerFactory entityManagerFactory;
    private Role role;

    @BeforeEach
    void setUp() {
        entityManagerFactory = createEntityManagerFactory("ru.ivamly");
        inTransaction(entityManager -> {
            role = new Role();
            role.setId(1L);
            role.setName("Admin");
            entityManager.persist(role);
        });
    }

    @AfterEach
    void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    void checkExistsBeforeAndAfterSave() {
        inTransaction(entityManager -> {
            assertFalse(entityManager.createQuery(QUERY, Boolean.class)
                    .setParameter("id", role.getId())
                    .getSingleResult());
            User user = new User();
            user.setId(1L);
            user.setName("Admin");
            user.setRole(role);
            entityManager.persist(user);
            entityManager.flush();
            assertTrue(entityManager.createQuery(QUERY, Boolean.class)
                    .setParameter("id", role.getId())
                    .getSingleResult());
        });
    }

    void inTransaction(Consumer<EntityManager> work) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            work.accept(entityManager);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
        finally {
            entityManager.close();
        }
    }
}
