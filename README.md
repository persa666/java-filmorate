# java-filmorate
Template repository for Filmorate project.
![База](https://github.com/persa666/java-filmorate/assets/119011873/08e972bd-f704-4827-a0b6-ef1b9581c56a)

1) Table films (Таблица "films"):

film_id - Уникальный идентификатор фильма (первичный ключ).
film_name - Название фильма.
film_description - Описание фильма.
film_release_date - Дата выпуска фильма.
film_duration - Продолжительность фильма.
film_mpa_id - Внешний ключ для таблицы mpa, поле с идентификатор оценки фильма.

2) Table users (Таблица "users"):

user_id - Уникальный идентификатор пользователя (первичный ключ).
user_name - Имя пользователя.
user_email - Адрес электронной почты пользователя.
user_login - Логин пользователя.
user_birthday - День рождения пользователя.

3) Table users_likes_films (Таблица "users_likes_films"):

film_id - Первичный ключ, связанный с таблицей film, указывает на фильм, который понравился пользователю.
user_id - Первичный ключ, связанный с таблицей user, указывает на пользователя, который оценил фильм.
Составной первичный ключ

4) Table genre (Таблица "genre"):

genre_id - Уникальный идентификатор жанра (первичный ключ).
name - Название жанра.

5) Table friends (Таблица "friends"):

user_id - Первичный ключ, связанный с таблицей user, указывает на пользователя, который имеет дружеские связи.
friend_id - Первичный ключ, идентификатор другого пользователя, с которым у пользователя есть дружеские связи.
friend_status - Поле статуса дружбы между пользователями (true или false).
Составной первичный ключ

6) Table film_genre (Таблица "film_genre"):

film_id - Первичный ключ, связанный с таблицей film, указывает на фильм.
genre_id - Первичный ключ, связанный с таблицей genre, указывает на жанр фильма.

7) Table mpa (Таблица "mpa"):

mpa_id - Первичный ключ, идентификатор райтинга
name - Название рейтинга

Примеры запросов:

Добавление фильма в список понравившихся пользователю:

INSERT INTO users_likes_films (user_id, film_id)
VALUES ([ID_пользователя], [ID_фильма]);

Обновление информации о фильме:

UPDATE films SET film_name = ?,
		 film_description = ?,
		 film_release_date = ?,
		 film_duration = ?,
		 film_mpa_id = ?
WHERE film_id = ?;

Обновление жанра фильма:

INSERT INTO film_genre (film_id, genre_id) 
VALUES (?, ?);

