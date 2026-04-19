///**
// * Пакет приложения.
// */
//package ru.yandex.practicum.filmorate;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.annotation.DirtiesContext;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//class FilmControllerTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
////POST
//
//    @Test
//    void testCreateFilm_ValidData_ShouldReturn200() {
//        Film film = new Film();
//        film.setName("Хороший фильм");
//        film.setDescription("Нормальное описание фильма");
//        film.setReleaseDate(LocalDate.of(1990, 1, 1));
//        film.setDuration(90L);
//
//        // Отправляем POST‑запрос на /films
//        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
//
//        assertEquals(200, response.getStatusCodeValue(),
//                "Ожидался статус 200 для созданного фильма фильма");
//
//        Film createdFilm = response.getBody();
//        assertNotNull(createdFilm);
//        assertNotNull(createdFilm.getId());
//        assertEquals("Хороший фильм", createdFilm.getName());
//    }
//
//    @Test
//    void testCreateFilm_EmptyName_ShouldReturn400() {
//        Film film = new Film();
//        film.setName("");
//        film.setDescription("Описание");
//        film.setReleaseDate(LocalDate.of(1990, 1, 1));
//        film.setDuration(90L);
//
//        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
//
//        assertEquals(400, response.getStatusCodeValue(),
//                "Ожидался статус 400 для пустого названия");
//    }
//
//    @Test
//    void testCreateFilm_DescriptionOver200_ShouldReturn400() {
//        Film film = new Film();
//        film.setName("Фильм");
//        film.setDescription("Х".repeat(201));
//        film.setReleaseDate(LocalDate.of(1990, 1, 1));
//        film.setDuration(90L);
//
//        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
//
//        assertEquals(400, response.getStatusCodeValue(),
//                "Ожидался статус 400 для описания больше 200 символов");
//    }
//
//    @Test
//    void testCreateFilm_Description200_ShouldReturn200() {
//        Film film = new Film();
//        film.setName("Фильм");
//        film.setDescription("Х".repeat(200));
//        film.setReleaseDate(LocalDate.of(1990, 1, 1));
//        film.setDuration(90L);
//
//        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
//
//        assertEquals(200, response.getStatusCodeValue(),
//                "Ожидался статус 200 для описания в 200 символов");
//    }
//
//    @Test
//    void testCreateFilm_InvalidReleaseDate_ShouldReturn400() {
//        Film film = new Film();
//        film.setName("Фильм");
//        film.setDescription("Описание");
//        film.setReleaseDate(LocalDate.of(1895, 12, 27));
//        film.setDuration(90L);
//
//        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
//
//        assertEquals(400, response.getStatusCodeValue(),
//                "Ожидался статус 400 для неподходящей даты релиза");
//    }
//
//    @Test
//    void testCreateFilm_ValidReleaseDate_ShouldReturn200() {
//        Film film = new Film();
//        film.setName("Фильм");
//        film.setDescription("Описание");
//        film.setReleaseDate(LocalDate.of(1895, 12, 28));
//        film.setDuration(90L);
//
//        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
//
//        assertEquals(200, response.getStatusCodeValue(),
//                "Ожидался статус 200 для корректной даты релиза");
//    }
//
//    @Test
//    void testCreateFilm_InValidDuration_ShouldReturn400() {
//        Film film = new Film();
//        film.setName("Фильм");
//        film.setDescription("Описание");
//        film.setReleaseDate(LocalDate.of(1895, 12, 28));
//        film.setDuration(-90L);
//
//        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
//
//        assertEquals(400, response.getStatusCodeValue(),
//                "Ожидался статус 400 для отрицательной продолжительности");
//    }
//
////PUT
//
//    @Test
//    void testUpdateFilm_ValidData_ShouldReturn200() {
//        Film filmToCreate = new Film();
//        filmToCreate.setName("Исходный фильм");
//        filmToCreate.setDescription("Исходное описание");
//        filmToCreate.setReleaseDate(LocalDate.of(1990, 1, 1));
//        filmToCreate.setDuration(90L);
//
//        ResponseEntity<Film> createResponse = restTemplate.postForEntity("/films", filmToCreate, Film.class);
//        Film createdFilm = createResponse.getBody();
//        Long filmId = createdFilm.getId();
//
//        Film updatedFilm = new Film();
//        updatedFilm.setId(filmId);
//        updatedFilm.setName("Обновлённое название");
//        updatedFilm.setDescription("Обновлённое описание");
//        updatedFilm.setReleaseDate(LocalDate.of(2000, 5, 15));
//        updatedFilm.setDuration(120L);
//
//        ResponseEntity<Film> updateResponse = restTemplate.exchange("/films", HttpMethod.PUT,
//                new HttpEntity<>(updatedFilm), Film.class);
//
//        assertEquals(200, updateResponse.getStatusCodeValue(),
//                "Ожидался статус 200 для обновленного фильма");
//
//        Film actualUpdatedFilm = updateResponse.getBody();
//        assertNotNull(actualUpdatedFilm);
//        assertEquals("Обновлённое название", actualUpdatedFilm.getName());
//        assertEquals("Обновлённое описание", actualUpdatedFilm.getDescription());
//        assertEquals(LocalDate.of(2000, 5, 15), actualUpdatedFilm.getReleaseDate());
//        assertEquals(120L, actualUpdatedFilm.getDuration());
//    }
//
//    @Test
//    void testUpdateFilm_NonExistentId_ShouldReturn404() {
//        Film film = new Film();
//        film.setId(9999L);
//        film.setName("Фильм с несуществующим ID");
//        film.setDescription("Описание");
//        film.setReleaseDate(LocalDate.of(1990, 1, 1));
//        film.setDuration(90L);
//
//        ResponseEntity<Film> response = restTemplate.exchange("/films", HttpMethod.PUT,
//                new HttpEntity<>(film), Film.class);
//
//        assertEquals(404, response.getStatusCodeValue(),
//                "Ожидался статус 404 для несуществующего ID");
//    }
//
////GET
//
//    @Test
//    void testGetAllFilms_WithData_ShouldReturn200() {
//
//        Film film1 = new Film();
//        film1.setName("Фильм 1");
//        film1.setDescription("Описание фильма 1");
//        film1.setReleaseDate(LocalDate.of(1995, 3, 15));
//        film1.setDuration(110L);
//
//        Film film2 = new Film();
//        film2.setName("Фильм 2");
//        film2.setDescription("Описание фильма 2");
//        film2.setReleaseDate(LocalDate.of(2000, 7, 20));
//        film2.setDuration(130L);
//
//        restTemplate.postForEntity("/films", film1, Film.class);
//        restTemplate.postForEntity("/films", film2, Film.class);
//
//        ResponseEntity<Film[]> response = restTemplate.getForEntity("/films", Film[].class);
//
//        assertEquals(200, response.getStatusCodeValue(),
//                "Ожидался статус 200 при получении списка фильмов");
//
//        Film[] films = response.getBody();
//        assertNotNull(films, "Тело ответа не должно быть null");
//        assertEquals(2, films.length, "Должно быть возвращено 2 фильма");
//
//        assertEquals("Фильм 1", films[0].getName(),
//                "Название первого фильма должно быть 'Фильм 1'");
//        assertEquals("Описание фильма 1", films[0].getDescription(),
//                "Описание первого фильма должно совпадать");
//        assertEquals(LocalDate.of(1995, 3, 15), films[0].getReleaseDate(),
//                "Дата релиза первого фильма должна совпадать");
//        assertEquals(110L, films[0].getDuration(),
//                "Длительность первого фильма должна быть 110 минут");
//
//        assertEquals("Фильм 2", films[1].getName(),
//                "Название второго фильма должно быть 'Фильм 2'");
//        assertEquals("Описание фильма 2", films[1].getDescription(),
//                "Описание второго фильма должно совпадать");
//        assertEquals(LocalDate.of(2000, 7, 20), films[1].getReleaseDate(),
//                "Дата релиза второго фильма должна совпадать");
//        assertEquals(130L, films[1].getDuration(),
//                "Длительность второго фильма должна быть 130 минут");
//    }
//}
//
