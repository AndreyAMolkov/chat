# chat
Страницы:
1.	Авторизация
На данной странице находятся поля ввода логина, пароля и кнопки войти и регистрация. При нажатии кнопки регистрация пользователь попадает на страницу регистрации. При нажатии кнопки войти происходит проверка корректности логина и пароля. В случае корректности – происходит переход на главную страницу авторизованного пользователя, в противном случае – ошибка.
2.	Регистрация
На данной странице находятся поля ввода логина, имени, фамилии, пароля, подтверждение пароля и кнопка регистрация. Необходимо проверять поле логина на уникальность, для пароля должна быть проверка на кол-во символов (не менее 8) на присутствие хотя бы одной цифры, строчной буквы, заглавной буквы и символа (из !@#$%). В случае корректности всех заполненных полей, происходит регистрация нового пользователя и переход на страницу логина.
3.	Главная страница авторизованного пользователя
На данной странице расположен список тем форума и кнопка создать тему. Темы отсортированы по дате последнего сообщения в них. При нажатии на тему, происходит переход на страницу этой темы. При нажатии кнопки создать тему, выводится окно с вводом названия и кнопками создать и отмена. При нажатии кнопки создать – происходит создание темы и переход на страницу этой темы. 
4.	Страница темы
На данной странице расположены сообщения пользователей, отсортированные по дате добавления. Каждое сообщение содержит имя пользователя и текст. Внизу расположено поле ввода сообщения и кнопка отправить.

Общие требования:
1.	Необходимо разграничить права доступа (админ, обычный пользователь). Обычный пользователь может создавать темы, писать сообщения в темах и удалять свои сообщения. Админ в дополнении к правам пользователя может удалять темы и сообщения любых пользователей.
2.	Необходимо сделать пейджинацию на странице с списком тем и на странице темы. 

Требования к разработке:
1.	Язык программирования – Java
2.	База данных – предпочтительно Postgres
3.	Фреймворки – Spring, Hibernate
