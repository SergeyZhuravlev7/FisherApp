INSERT INTO users (login, password_hash, name, birthdate, gender, created_at, email, role)
VALUES ('MissJane',
'$2a$12$OcKrhLfcQvEXQqZX0EiZQu6nemwlAYiwoS9vQPpesf91ZwxBo23EK',
'Евгения Морозова',
'1991-02-12',
'FEMALE',
now(),
'testmail1@gmail.com',
"USER"),
('StepanRazin',
'$2a$12$OcKrhLfcQvEXQqZX0EiZQu6nemwlAYiwoS9vQPpesf91ZwxBo23EK',
'Степан Иванов',
'1993-05-18',
'MALE',
now(),
'testmail2@gmail.com',
"USER"),
('Admin123',
'$2a$12$jD469nUpvmDVWfLUt6lvUebJTv9FsVjoknKJkXlUMofAepCD1ZLfa',
'Сергей Журавлев',
'1995-02-08',
'MALE',
now(),
'testmail3@gmail.com',
'ADMIN');

INSERT INTO achievements (name, description) VALUES ('Тяжеловес', 'Поймана рыба весом более 15 килограмм'),
('Я памятник себе воздвиг нерукотворный...', 'Написано больше 100 постов');

INSERT INTO posts (user_id, fish, fish_weight, message, likes_count)
VALUES (1, 'Щука', 4.85, 'Test message', 1),
(1, 'Сазан', 2.12, 'Test message', 1),
(2, 'Карп', 3.90, 'Test message', 3),
(2, 'Cом', 30.8, 'Test message', 2);

INSERT INTO user_achievements VALUES (2, 1), (2, 2);

INSERT INTO comments (user_id, post_id, message) VALUES (1,2,'Вау, вот это повезло.'), (1,2,'Вау, вот это повезло.');

INSERT INTO likes (user_id, post_id) VALUES(1,1),(1,2),(1,3),(1,4),(2,3),(3,3),(2,4);