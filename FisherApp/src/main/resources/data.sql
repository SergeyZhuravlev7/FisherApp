INSERT INTO user (login, password_hash, name, age, gender, created_at)
VALUES ('MissJane',
'$2a$12$OcKrhLfcQvEXQqZX0EiZQu6nemwlAYiwoS9vQPpesf91ZwxBo23EK',
'Евгения Морозова',
30,
'женщина',
now()),
('StepanRazin',
'$2a$12$OcKrhLfcQvEXQqZX0EiZQu6nemwlAYiwoS9vQPpesf91ZwxBo23EK',
'Степан Иванов',
28,
'мужчина',
now());

INSERT INTO fish (name) VALUES ('Щука'), ('Сазан'), ('Окунь'), ('Сом');

INSERT INTO achievements (name, description) VALUES ('Тяжеловес', 'Поймана рыба весом более 15 килограмм'),
('Я памятник себе воздвиг нерукотворный...', 'Написано больше 100 постов');

INSERT INTO posts (user_id, fish_id, fish_weight)
VALUES (1, 2, 4.85),
(1, 1, 2.12),
(2, 1, 3.90),
(2, 4, 30.8);