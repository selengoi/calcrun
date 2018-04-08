## Описание
* Многопоточное приложение для обработки очереди задач, запускается внутри сервера приложений
* Spring, Spring JMX, Hibernate, slf4j+log4j
* Задания регистрируются в пуле задач
* Пул задач может состоять из заданий 2х типов: REGULAR (исполнение через ThreadPoolExecutor), SCHEDULED (выполняются scheduler'ом)
* Управление через JMX: Запуск/остановка обработки очереди задач; запуск/остановка connection pool; изменение уровня логирования; просмотр пула задач
* Логирование в БД и на файловой системе через log4j


## Пакеты
* ru.corp.az.azrun.common.db: классы для работы с connection pool
* ru.corp.az.azrun.common.dispose: осовобождение ресурсов 
* ru.corp.az.azrun.common.log4j12: custom appender log4j
* ru.corp.az.azrun.common.taskx: абстрактные классы задач, пул задач
* ru.corp.az.azrun.azcalc.dao: Data Access. Зависимый от БД код вынесен в отдельные классы
* ru.corp.az.azrun.azcalc.task: расчетные задания (запуск с помощью ThreadPoolExecutor на основе PriorityBlockingQueue), задания для наполнения внутренней очереди ThreadPoolExecutor (запуск по расписанию)
* ru.corp.az.azrun.jmx: jmx mbean