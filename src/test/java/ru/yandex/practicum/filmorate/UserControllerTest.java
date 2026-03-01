/**
 * Пакет приложения.
 */
package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_NOT_FOUND = 500;


    @Autowired
    private TestRestTemplate restTemplate;

// POST

    @Test
    void testCreateUser_ValidData_ShouldReturn200() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("user");
        user.setName("Пользователь");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HTTP_STATUS_OK, response.getStatusCodeValue(),
                "Ожидался статус 200 для созданного пользователя");

        User createdUser = response.getBody();
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals("mail@mail.ru", createdUser.getEmail());
        assertEquals("user", createdUser.getLogin());
        assertEquals("Пользователь", createdUser.getName());
    }

    @Test
    void testCreateUser_EmptyEmail_ShouldReturn500() {
        User user = new User();
        user.setEmail("");
        user.setLogin("user");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatusCodeValue(),
                "Ожидался статус 500 для пустого email");
    }

    @Test
    void testCreateUser_InvalidEmail_ShouldReturn500() {
        User user = new User();
        user.setEmail("mail.ru");
        user.setLogin("user");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatusCodeValue(),
                "Ожидался статус 500 для некорректного email");
    }

    @Test
    void testCreateUser_DuplicateEmail_ShouldReturn500() {
        User user1 = new User();
        user1.setEmail("mail@mail.ru");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        restTemplate.postForEntity("/users", user1, User.class);

        User user2 = new User();
        user2.setEmail("mail@mail.ru");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.of(1995, 5, 5));

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user2, User.class);

        assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatusCodeValue(),
                "Ожидался статус 500 для дублирующегося email");
    }

    @Test
    void testCreateUser_EmptyLogin_ShouldReturn500() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatusCodeValue(),
                "Ожидался статус 500 для пустого login");
    }

    @Test
    void testCreateUser_LoginWithSpaces_ShouldReturn500() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("user with spaces");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatusCodeValue(),
                "Ожидался статус 500 для login с пробелами");
    }

    @Test
    void testCreateUser_NullBirthday_ShouldReturn500() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("user");
        user.setBirthday(null);

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatusCodeValue(),
                "Ожидался статус 500 для отсутствующей даты рождения");
    }

    @Test
    void testCreateUser_FutureBirthday_ShouldReturn500() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("user");
        user.setBirthday(LocalDate.now().plusDays(1));


        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatusCodeValue(),
                "Ожидался статус 500 для даты рождения в будущем");
    }

    @Test
    void testCreateUser_NullName_ShouldUseLoginAsName() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("login");
        user.setName(null);
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HTTP_STATUS_OK, response.getStatusCodeValue());

        User createdUser = response.getBody();
        assertEquals("login", createdUser.getName(),
                "Если имя не указано, должно использоваться значение login");
    }

// PUT

    @Test
    void testUpdateUser_ValidData_ShouldReturn200() {
        User userToCreate = new User();
        userToCreate.setEmail("old@mail.ru");
        userToCreate.setLogin("oldlogin");
        userToCreate.setName("Старое имя");
        userToCreate.setBirthday(LocalDate.of(1990, 1, 1));

        ResponseEntity<User> createResponse = restTemplate.postForEntity("/users", userToCreate, User.class);
        User createdUser = createResponse.getBody();
        Long userId = createdUser.getId();

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail("updated@mail.ru");
        updatedUser.setLogin("updatedlogin");
        updatedUser.setName("Обновлённое имя");
        updatedUser.setBirthday(LocalDate.of(1995, 5, 5));

        ResponseEntity<User> updateResponse = restTemplate.exchange("/users", HttpMethod.PUT,
                new HttpEntity<>(updatedUser), User.class);


        assertEquals(HTTP_STATUS_OK, updateResponse.getStatusCodeValue(),
                "Ожидался статус 200 для обновлённого пользователя");

        User actualUpdatedUser = updateResponse.getBody();
        assertNotNull(actualUpdatedUser);
        assertEquals("updated@mail.ru", actualUpdatedUser.getEmail());
        assertEquals("updatedlogin", actualUpdatedUser.getLogin());
        assertEquals("Обновлённое имя", actualUpdatedUser.getName());
        assertEquals(LocalDate.of(1995, 5, 5), actualUpdatedUser.getBirthday());
    }

    @Test
    void testUpdateUser_DuplicateEmail_ShouldReturn500() {
        User user1 = new User();
        user1.setEmail("mail1@mail.ru");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        ResponseEntity<User> createResponse1 = restTemplate.postForEntity("/users", user1, User.class);
        User createdUser1 = createResponse1.getBody();

        // Создаём второго пользователя
        User user2 = new User();
        user2.setEmail("mail2@mail.ru");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.of(1995, 5, 5));
        restTemplate.postForEntity("/users", user2, User.class);

        // Пытаемся обновить email первого пользователя на email второго
        createdUser1.setEmail("mail2@mail.ru");

        ResponseEntity<User> updateResponse = restTemplate.exchange("/users", HttpMethod.PUT,
                new HttpEntity<>(createdUser1), User.class);

        assertEquals(HTTP_STATUS_NOT_FOUND, updateResponse.getStatusCodeValue(),
                "Ожидался статус 500 для дублирующегося email при обновлении");
    }

//GET

    @Test
    void testGetAllUsers_WithData_ShouldReturn200() {
        User user1 = new User();
        user1.setEmail("mail1@mail.ru");
        user1.setLogin("login1");
        user1.setName("Пользователь 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        restTemplate.postForEntity("/users", user1, User.class);

        User user2 = new User();
        user2.setEmail("mail2@mail.ru");
        user2.setLogin("login2");
        user2.setName("Пользователь 2");
        user2.setBirthday(LocalDate.of(1995, 5, 5));
        restTemplate.postForEntity("/users", user2, User.class);

        ResponseEntity<User[]> response = restTemplate.getForEntity("/users", User[].class);

        assertEquals(HTTP_STATUS_OK, response.getStatusCodeValue(),
                "Ожидался статус 200 при получении списка пользователей");

        User[] users = response.getBody();
        assertNotNull(users, "Тело ответа не должно быть null");
        assertEquals(2, users.length, "Должно быть возвращено 2 пользователя");

        assertEquals("mail1@mail.ru", users[0].getEmail());
        assertEquals("login1", users[0].getLogin());
        assertEquals("Пользователь 1", users[0].getName());
        assertEquals(LocalDate.of(1990, 1, 1), users[0].getBirthday());

        assertEquals("mail2@mail.ru", users[1].getEmail());
        assertEquals("login2", users[1].getLogin());
        assertEquals("Пользователь 2", users[1].getName());
        assertEquals(LocalDate.of(1995, 5, 5), users[1].getBirthday());
    }

    @Test
    void testGetAllUsers_EmptyList_ShouldReturn200() {
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users", User[].class);

        assertEquals(HTTP_STATUS_OK, response.getStatusCodeValue(),
                "Ожидался статус 200 при получении пустого списка пользователей");

        User[] users = response.getBody();
        assertNotNull(users, "Тело ответа не должно быть null");
        assertEquals(0, users.length, "При пустом списке должно возвращаться 0 пользователей");
    }
}
