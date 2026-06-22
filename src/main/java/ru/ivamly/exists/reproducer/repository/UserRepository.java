package ru.ivamly.exists.reproducer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ivamly.exists.reproducer.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
