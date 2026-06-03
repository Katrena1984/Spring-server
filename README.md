# Auth Backend API
Spring Boot JWT Authentication Service with HttpOnly cookies

# Описание
REST API для аутентификации и регистрации пользователей с использованием JWT токенов и HttpOnly cookies + система ролей

## Технологии
- **Java 17**
- **Spring Boot 3.x**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **PostgreSQL**
- **Hibernate/JPA**
- **Lombok**
- **Maven**
- **JavaScript**
- **React (Vite)**
- **HTML + CSS**
- **Docker**
- **DBeaver**

## Функционал
 Регистрация пользователей  
 Вход/выход (Login/Logout)  
 JWT аутентификация  
 HttpOnly cookies (защита от XSS)  
 Refresh + access токены  
 Роли (ADMIN, EDITOR, VIEWER)  
 BCrypt хэширование паролей  
 Защита от CSRF атак

## Архитектура проекта
controller: REST-эндпоинты, обработка HTTP-запросов и валидация входных DTO.
service: Бизнес-логика (аутентификация, регистрация, генерация токенов).
repository: Уровень доступа к данным через Spring Data JPA.
config: Конфигурация приложения (Security, CORS, инициализация БД).
dto: Объекты передачи данных (Data Transfer Objects).
entity: JPA-сущности, маппирующие таблицы базы данных.

## API Эндпоинты
/auth/registration - регистрация
/auth/sign-in - вход
/user/profile - профильъ
/auth/logout - выход

### Требования:
- Java 17+
- PostgreSQL 14+
- Maven 3.8+



