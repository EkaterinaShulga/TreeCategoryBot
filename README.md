
# Telegram-bot для управления деревом категорий

**Данный бот написан для создания дерева категорий.**

Боты — специальные аккаунты в Telegram, созданные для того, чтобы автоматически обрабатывать и отправлять сообщения.
Пользователи могут взаимодействовать с ботами при помощи сообщений, отправляемых через обычные или групповые чаты.
Логика бота контролируется при помощи HTTPS запросов к нашему API для ботов.
## Содержание

- [Что умеет бот?](#что-умеет-бот)
- [Как использовать?](#как-использовать)



## FAQ

### Что умеет бот?

Бот реализует функционал для пользователя, и предоставляет возможность создать свое дерево категорий
(например, вносить информацию о товарах в магазине и в дальнейшем, при необходимости, 
данную информацию просматирвать и изменять)

Пользователь может:

- получать инфорацию о внесенных в базу категориях и подкатегориях(в структурированном виде),
  если информаци нет, бот об этом сообщит
- добавлять категории, при условии, что ранее они не были добавлены
- добавлять подкатегории (бот проверит уникальность подкатегории (чтобы не было дублирования информации)
- удалять любую подкатегорию и  категорию (относящиеся к ней подкатегории удаляются автоматически)
- ознакомиться со всем функционалом бота, нажав соотствующую кнопку меню
### Как использовать?

- Поиск через юзернейм

  В строке «Поиск» нужно ввести @имя_бота_bot;

  Дальше следует нажать на клавишу «Запустить».

**Технологии, использованные в проекте:**<br>
Язык: Java<br>
СУБД: PostgreSQL

#### [Исполнитель](#наша-команда)
[Екатерина Шульга](https://github.com/EkaterinaShulga)  