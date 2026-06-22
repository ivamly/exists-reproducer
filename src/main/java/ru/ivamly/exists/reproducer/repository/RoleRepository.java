package ru.ivamly.exists.reproducer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ivamly.exists.reproducer.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select exists (select 1 from User u where u.role.id = :id)")
    boolean exists(Long id);
}
