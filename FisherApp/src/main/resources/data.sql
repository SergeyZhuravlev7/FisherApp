INSERT INTO users (login, password_hash, name, age, gender, created_at, role)
VALUES ('MissJane',
'$2a$12$OcKrhLfcQvEXQqZX0EiZQu6nemwlAYiwoS9vQPpesf91ZwxBo23EK',
'Евгения Морозова',
30,
'FEMALE',
now(),
"USER"),
('StepanRazin',
'$2a$12$OcKrhLfcQvEXQqZX0EiZQu6nemwlAYiwoS9vQPpesf91ZwxBo23EK',
'Степан Иванов',
28,
'MALE',
now(),
"USER");

INSERT INTO achievements (name, description) VALUES ('Тяжеловес', 'Поймана рыба весом более 15 килограмм'),
('Я памятник себе воздвиг нерукотворный...', 'Написано больше 100 постов');

INSERT INTO posts (user_id, fish, fish_weight, message)
VALUES (1, 'Щука', 4.85, 'Test message'),
(1, 'Сазан', 2.12, 'Test message'),
(2, 'Карп', 3.90, 'Test message'),
(2, 'Cом', 30.8, 'Test message');

INSERT INTO user_achievements VALUES (2, 1), (2, 2);