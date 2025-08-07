package ru.zhuravlev.FisherApp.Models;


/* author--> 
Sergey Zhuravlev
*/

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public enum Fish {
    FISH1("Щука"),
    FISH2("Окунь"),
    FISH3("Сом"),
    FISH4("Плотва"),
    FISH5("Лещ"),
    FISH6("Карась"),
    FISH7("Сазан"),
    FISH8("Судак"),
    FISH9("Налим"),
    FISH10("Хариус"),
    FISH11("Жерех"),
    FISH12("Язь"),
    FISH13("Голавль"),
    FISH14("Линь"),
    FISH15("Муксун"),
    FISH16("Нельма"),
    FISH17("Ряпушка"),
    FISH18("Ротан"),
    FISH19("Горчак"),
    FISH20("Белый амур"),
    FISH21("Толстолобик"),
    FISH22("Чехонь"),
    FISH23("Подуст"),
    FISH24("Уклейка"),
    FISH25("Бычок"),
    FISH26("Сельдь"),
    FISH27("Севрюга"),
    FISH28("Осётр"),
    FISH29("Форель"),
    FISH30("Сиг"),
    FISH31("Лосось");

    private final String displayName;

    Fish(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static final List<String> list = Stream.of(Fish.values()).map(Fish::getDisplayName).sorted().toList();

    public static List<String> getFishNames() {
        return list;
    }
}

