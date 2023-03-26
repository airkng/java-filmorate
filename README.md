# java-filmorate
Template repository for Filmorate project.

## Проектирование базы данных
---
Жанры и рейтинг были вынесены в отдельную таблицу, поскольку это отдельные сущности, их определенное количество, и в теории, они могу поменяться. И лучше всего фильму иметь айдишники жанров и рейтингов, чтобы потом было проще менять. Короче говоря, чтобы отсутствовала аномалия и избыточность данных.

Таблица like_list является посредником между User и Film. И по сути просто реализует связь "Многие ко многим".

Стоит **обязательно упомянуть** таблицу friend_list. У него двойная связь с таблицей User. При создании пользователя User, помимо user_id ему присваивается идентификатор friend_id. На практике эти значения будут равны. friend_id нужен для того, чтобы в таблице friend_list мы понимали с кем дружит Пользователь с user_id.

*Как вариант это можно было бы реализовать через двойную связь в таблице User с полем user_id*

[таблица](https://github.com/airkng/java-filmorate/blob/database-design/src/main/resources/database-design.png) <br>
![f](https://dbdiagram.io/d/641f2fc15758ac5f17241887)
