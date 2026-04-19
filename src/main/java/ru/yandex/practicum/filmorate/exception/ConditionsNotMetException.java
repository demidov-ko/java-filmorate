//package ru.yandex.practicum.filmorate.exception;
//
////при создании нового поста, проверяется бизнес-правило — нельзя добавлять пост без описания.
//// Поэтому, если пост не содержит описание или это пустая строка, генерируется исключение ConditionsNotMetException.
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
//public class ConditionsNotMetException extends RuntimeException {
//    public ConditionsNotMetException(String message) {
//        super(message);
//    }
//}
