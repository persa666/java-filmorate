# java-filmorate
Template repository for Filmorate project.
![бдласт](https://github.com/persa666/java-filmorate/assets/119011873/f855c6ce-6dc0-45b2-a819-4d41d4e93edb)

1) Table film (Таблица "film"):

film_id - Уникальный идентификатор фильма (первичный ключ).
film_name - Название фильма.
film_description - Описание фильма.
film_release_date - Дата выпуска фильма.
film_duration - Продолжительность фильма.
film_rating - Поле с перечислением (Enum_rating) оценка фильма.

2) Table user (Таблица "user"):

user_id - Уникальный идентификатор пользователя (первичный ключ).
user_name - Имя пользователя.
user_email - Адрес электронной почты пользователя.
user_login - Логин пользователя.
user_birthday - День рождения пользователя.
Table users_likes_films (Таблица "users_likes_films"):

film_id - Внешний ключ, связанный с таблицей film, указывает на фильм, который понравился пользователю.
user_id - Внешний ключ, связанный с таблицей user, указывает на пользователя, который оценил фильм.

3) Table genre (Таблица "genre"):

genre_id - Уникальный идентификатор жанра (первичный ключ).
name - Название жанра.

4) Table friends (Таблица "friends"):

user_id - Внешний ключ, связанный с таблицей user, указывает на пользователя, который имеет дружеские связи.
friend_id - Идентификатор другого пользователя, с которым у пользователя есть дружеские связи.
friend_status - Поле с перечислением (Enum_status) статус дружбы между пользователями.

5) Table film_genre (Таблица "film_genre"):

film_id - Внешний ключ, связанный с таблицей film, указывает на фильм.
genre_id - Внешний ключ, связанный с таблицей genre, указывает на жанр фильма.


Примеры запросов:

Выборка всех фильмов определенного жанра:

SELECT film_name, film_description
FROM film
INNER JOIN film_genre ON film.film_id = film_genre.film_id
WHERE film_genre.genre_id = [ID_жанра];

Добавление фильма в список понравившихся пользователю:

INSERT INTO users_likes_films (user_id, film_id)
VALUES ([ID_пользователя], [ID_фильма]);

Обновление информации о фильме:

UPDATE film
SET film_name = 'Новое название фильма'
WHERE film_id = [ID_фильма];

Выборка количества пользователей, лайкнувших определенный фильм:

SELECT COUNT(user_id)
FROM users_likes_films
WHERE film_id = [ID_фильма];