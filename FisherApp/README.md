Добавил JWT аутентификацию, поэтому после отправки корректных данных на api/auth/login в ответ придет 2 токена -
AccessToken и RefreshToken.
У токена доступа короткий срок жизни порядка 15 минут, у токена обновления срок жизни 100 дней.
Теперь юзер считается аутентифицированным, если с запросом он посылает заголовок "Authorization" который содержит в
себе "Bearer значение_токена"/
Если токен не валидный придет ответ с заголовком "AccessToken" и значением "Expire". В таком случае надо послать запрос
на /api/auth/token с телом "RefreshToken", и если
токен валидный, в ответ придет новый "AccessToken".

Эндпойнты:
/api/auth/registration - POST запрос. Сюда отправляется валидный юзер для регистрации.

Валидация юзеров следующая:
login - от 8 до 30 символов, не может состоять из пробелов и быть пустым.
name - от 3 до 30 символов, не может состоять из пробелов и быть пустым.
password - от 8 символов длиной, может состоять из больших и маленьких букв латинского алфавита, а также
спецсимволов !?#$%*().
birthDate пока без валидации.
gender - "MALE" или "FEMALE".

Тело запроса:
{
"login":"loginnnn",
"password":"testpassword",
"name":"ТестИмя",
"birthDate":"08.02.1995",
"email" : "testmail@gmail.com",
"gender":"MALE"
}

Если логин уже используется, возвращает bad request и сообщение о том, что логин уже используется.
Если поля не проходят валидацию, возвращает bad request и сообщение о том, в каких полях нарушена валидация.
В случае успеха возвращает статус 200.

/api/auth/login - POST запрос. Здесь реализована логика аутентификации пользователя.
Отправляется логин и пароль, валидация такая же как в регистрации.

Тело запроса.

{
"login": "loginnnn",
"password": "password"
}

Если поля не проходят валидацию, возвращает bad request и сообщение о том, в каких полях нарушена валидация.
Если юзер не найден, вовзращает bad request и сообщение о том, что учетные данные не совпадают.
В случае успеха возвращает статус 200 и 2 токена "AccessToken" и "RefreshToken".

Пример ответа:

{
"accessToken": "
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIyNzhhYTU4NC1jMzIzLTQ1Y2QtODUyMi03OTRkZTM1MjczNzAiLCJhbGciOiJITUFDMjU2IiwidHlwIjoiYWNjZXNzIiwiaXNzIjoiRmlzaGVyQXBwIiwiaWF0IjoxNzU0NDg4MjIzLCJzdWIiOiJTdGVwYW5SYXppbiIsIlJPTEVTIjpbIlVTRVIiXSwiZXhwIjoxNzU0NDg4MzEzfQ.3KWbfWDw_TUQbdpFAnEttYnw7z_WTBbgLE2X45hYUWk",
"refreshToken": "
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIxZTI1MDYxZS0xMDcyLTQ5YmEtYmRkYi01ZTQ4NDEzYTljYTYiLCJhbGciOiJITUFDMjU2IiwidHlwIjoicmVmcmVzaCIsImlzcyI6IkZpc2hlckFwcCIsImlhdCI6MTc1NDQ4ODIyMywic3ViIjoiU3RlcGFuUmF6aW4iLCJST0xFUyI6WyJVU0VSIl0sImV4cCI6MTc1NzA4MDIyM30.dAWQ7e9MwiiI1m3Ie_GjdmWLdRVE1lVHDrqWM0OchKw"
}

/api/auth/token - POST запрос, здесь реализована логика обновления AccessToken по RefreshToken. Если RefreshToken
валидный в ответ придет новый AccessToken.
Требует тело запроса, содержащее RefreshToken.

Тело запроса

{
"refreshToken": "
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIxZTI1MDYxZS0xMDcyLTQ5YmEtYmRkYi01ZTQ4NDEzYTljYTYiLCJhbGciOiJITUFDMjU2IiwidHlwIjoicmVmcmVzaCIsImlzcyI6IkZpc2hlckFwcCIsImlhdCI6MTc1NDQ4ODIyMywic3ViIjoiU3RlcGFuUmF6aW4iLCJST0xFUyI6WyJVU0VSIl0sImV4cCI6MTc1NzA4MDIyM30.dAWQ7e9MwiiI1m3Ie_GjdmWLdRVE1lVHDrqWM0OchKw"
}

Пример ответа:

{
"accessToken": "
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIxOGFlOTU3ZS1jZDAzLTRhYTYtOGU1ZC0wMzc4NjhiMDlkNGEiLCJhbGciOiJITUFDMjU2IiwidHlwIjoiYWNjZXNzIiwiaXNzIjoiRmlzaGVyQXBwIiwiaWF0IjoxNzU0NDg4NDQwLCJzdWIiOiJTdGVwYW5SYXppbiIsIlJPTEVTIjpbIlVTRVIiXSwiZXhwIjoxNzU0NDg4NTMwfQ.zFiT8kJMC5cyFpYwJ7se5CTKi8elfTWncj6fiMScjvg"
}

Если токен не валидный статус 400 и сообщение "Токен не соответствует формату или кодировке."
Если срок токена истек, то статус 401 и все.
В случае успеха возвращает статус 200 и токен "accessToken".

/api/users/{login} - GET запрос. Возвращает страницу пользователя по логину, т.е. его профиль, если страница существует,
ничего не нужно кроме логина в пути.

Пример запроса: /api/users/StepanRazin/

Пример ответа: Юзер, его посты, название рыбы и ее вес в посте, достижения юзера.
{
"login": "StepanRazin",
"name": "Степан Иванов",
"age": 28,
"gender": "MALE",
"posts": [
{
"id": 3,
"fish": "Щука",
"fish_weight": 3.90,
"message": "Test message"
},
{
"id": 4,
"fish":"Сом",
"fish_weight": 30.80,
"message": "Test message"
}
],
"achievements": [
{
"name": "Тяжеловес",
"description": "Поймана рыба весом более 15 килограмм"
},
{
"name": "Я памятник себе воздвиг нерукотворный...",
"description": "Написано больше 100 постов"
}
]
}

Если пользователь с таким логином отсутствует, возвращает bad request и сообщение о том, что пользователь с таким
логином не зарегистрирован.

/api/users/{login} - DELETE запрос. Удаляет страницу пользователя, работает только в том случае, если
аутентифицированный пользователь удаляет свою страницу.
Ничего не нужно кроме логина в пути.
Если логин в адресе и аутентификация не совпадают, то ошибка bad request и сообщение о том, что отсутствуют
права на доступ к этой странице, иначе статус 200, независимо от того, есть такой пользователь или нет.

Пример запроса: /api/users/StepanRazin/

/api/users/{login}/posts - POST запрос. Нужен логин в пути. Добавляет новый пост пользователю, работает только в том
случае, если аутентифицированный пользователь добавляет
пост на свою страницу. Пост должен быть валидным.

Валидация постов следующая:
Название рыбы - из согласованного списка.
Вес рыбы - минимальное значение 0, формат с плавающей точкой с точностью до 2 знаков после запятой. Например: 4.85,
99.99.
Текст поста - от 1 до 300 символов.
Может быть пост без рыбы и веса рыбы, только сообщение, но нельзя добавить рыбу без веса и вес без рыбы.

Пример запроса: /api/users/StepanRazin/posts/
Тело запроса:

{
"fish" : "Щука",
"fish_weight" : 8.88,
"message" : "Ух какую рыбину поймал."
}

Если пост не прошел валидацию, возвращает bad request и сообщение о том, в каких полях нарушена валидация.

/api/users/{login}/posts/{postId} - DELETE запрос. Удаляет этот пост со страницы пользователя, работает только в том
случае, если аутентифицированный
пользователь удаляет пост со своей страницы.
Нужен логин в пути и ID поста, который нужно удалить.

Пример запроса: /api/users/StepanRazin/posts/4

В ответ всегда статус 200.



