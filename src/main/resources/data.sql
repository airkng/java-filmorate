DELETE FROM film_genres;
DELETE FROM like_list;
DELETE FROM film;
DELETE FROM mpa_rating;
DELETE FROM genre;
DELETE FROM friends_list;
DELETE FROM users;



MERGE INTO mpa_rating (rating_id, rating_name)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');


MERGE INTO genre(genre_id, genre_name)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO status(status_id, name)
VALUES (0, false),
       (1, true);

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;
ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1;
ALTER TABLE film_genres ALTER COLUMN id RESTART WITH 1;
ALTER TABLE like_list ALTER COLUMN id RESTART WITH 1;
ALTER TABLE friends_list ALTER COLUMN id RESTART WITH 1;
