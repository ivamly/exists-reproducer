package ru.ivamly.exists.reproducer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.ivamly.exists.reproducer.entity.Role;
import ru.ivamly.exists.reproducer.entity.User;
import ru.ivamly.exists.reproducer.repository.RoleRepository;
import ru.ivamly.exists.reproducer.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ExistsTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("Admin");
        roleRepository.save(role);
    }

    @Test
    @Transactional
    void checkExistsBeforeAndAfterSave() {
        assertFalse(roleRepository.exists(role.getId()));
        User user = new User();
        user.setId(1L);
        user.setName("Admin");
        user.setRole(role);
        userRepository.saveAndFlush(user);
        assertTrue(roleRepository.exists(role.getId()));
    }
}
