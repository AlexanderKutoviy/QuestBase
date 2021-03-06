define(["lib/model"],function(m) {
    return function() {
        return new m.Model(({
            questions: [
                {
                    id: "q0",
                    text: "Ви хочете пройти опитування?",
                    type: "RadioGroup",
                    options: {"items": ["Так", "Ні"]},
                    //shuffleItems:true,
                    perm: true,
                    perm: false,
                    perm: {exceptLast: 1},
                    perm: {exceptFirst: 1},
                    perm: {exceptFirst: 1, exceptLast: 1},
                    perm: {except: [5, 4]},
                },
                {
                    id: "q1",
                    text: "Яка твоя стать?",
                    type: "RadioGroup",
                    options: {"items": ["Жіноча", "Чоловіча"]},
                    options: {"items": [{id: 0, text: "Жіноча"}, {id: 1, text: "Чоловіча"}]},
                    // 0 - Жіноча
                    // 1 - Чоловіча
                    cond: function (model) {
                        return model.getAnswer("q0") == 0;
                    }
                },
                {
                    id: "q2",
                    text: "Яка твоя стать?",
                    type: "RadioGroup",
                    options: function (model) {

                    },
                    cond: function (model) {
                        return model.getAnswer("q0") == 0;
                    }
                },
                {
                    id: "qName",
                    text: "Яке твоє ім'я?",
                    type: "TextInput",
                    options: {},
                },
                {
                    id: "q4",
                    text: "Яке ім'я ти вибрав би для своєї дитини?",
                    type: "RadioGroup",
                    options: function (model) {
                        var name = model.getAnswer("qName");
                        return {items: [{id: 1, text: "Вася"}, {id: "qName", text: name}, {id: "other", text: "Інше"}]};
                        //return  {items:["Вася", "-твоє ім'я-","Інше"]};
                    },
                    options: {
                        items: [
                            {id: 1, text: "Вася"},
                            {
                                id: "qName",
                                text: function (model) {
                                    return model.getAnswer("qName");
                                },
                                visible: function (model) {
                                    return !!model.getAnswer("qName");
                                }
                            },
                            {id: "other", text: "Інше"}
                        ]
                    },
                    // 0 - Вася
                    // 1 - <відповідь на питання qName>
                    // 2 - Інше
                    metadata: [
                        [{id: 1, text: "Вася"}, {id: "q<name>", text: "<відповідь на питання q<name>>"}, {
                            id: "other",
                            text: "Інше"
                        }]
                    ]
                }
            ],
            analyser: {
                class: 'PersonalForm',
                "layout": "result3",
                "answers": {
                    1: ["Дуротан Тягнибок", [-1, 1, 2, 1, -1, -2], [-1, 2, 0, 0], [-2, -1, 1, 1], [1, -1, 1, 1, 2], [1, 1, 2, -1], [2, 1, 2, -1], [2, -1, 1, 1, -1], "Один із найсильніших гравців у політичному світі WoWу. Його демедж настільки руйнівний, що кожна одиниця урону для інших гравців може стати фатальною. Не любить зрадників та брехунів, проте сам час від часу цими прийомами користується. У великому світі політичного WoWу його найсильнішою зброєю є тризубий меч. "],
                    2: ["Рексар Путін", [1, 1, 1, -1, 1, 2], [1, -2, 1, -1], [2, 1, 2, -2], [1, 1, 1, 1, -1], [2, 2, 1, -2], [-1, 1, 1, 2], [1, -1, 1, 2, -1], "Найжорстокіший з роду мисливців. Його сила -  бомбити території ворогів і при тому не залишати слідів. Може швидко перетворюватись у звірів, яких приручив - саме тому його майже неможливо впіймати на гарячому. Ворожі орки вже розробляють план його захоплення -  хп на межі."],
                    3: ["Тралл Корбан", [-2, 1, 2, 1, -1, 1], [2, 2, 0, 1], [-1, 1, 1, 2], [-2, 1, 1, -1, 2], [2, 2, -1, -2], [1, 2, 1, 0], [-2, -2, 2, -1, 2], "Шаман - той, хто найкраще вміє опановувати стихії та керувати тотемами. Його слухається вогонь і рослини, у нього великий фундамент підтримки. Проте найбільша його зброя - троллята. Маленькі істоти, які залоскочують противника до смерті."],
                    4: ["Волджин Обама", [1, 1, -2, -1, 1, 0], [2, 2, 1, -1], [-2, -1, 1, 2], [1, 1, 1, -1, 2], [1, 1, 2, -1], [-1, 2, -1, 1], [-1, -1, 2, -1, 1], "Похмурий власник підземелля Наксрамасу. Він - повелитель павуків та Лотхіба, які у тандемі вже кілька років відбивають усі ворожі атаки. Найнеприступніший з усіх відомих героїв. "],
                    5: ["Утер Ляшко", [-2, 1, -2, 1, 2, -1], [1, -2, 1, 0], [0, 0, 1, 1], [2, -2, 1, -1, 1], [-2, -1, 1, -1], [-1, 1, 2, -2], [2, 1, -2, 1, 1], "Бафнутий на всю голову паладін з видозміненими вилами. Хоч і тримає він у руках досить пристойну зброю, його найбільшою силою і захистом є спели - ті, які він краде у магів, друїдів та шаманів. Його хп завжди майже на межі, проте у нього багато золота, яке часто його лікує. Від несподіваної атаки захищає божественний щит."],
                    6: ["Гулдан Думчев", [-1, 1, 1, 1, 2, 1], [-1, 1, -1, 2], [1, 2, -2, 1], [-2, 2, 1, -1, -1], [1, -1, 2, 1], [1, 2, 1, 0], [1, -2, -1, 2, 1], "Найжадібніший герой, який заради влади та сили готовий продати усе, навіть душу дияволу. Він продався демонам, які зробили з нього підвладного монстра. Без власної думки та волі він просто маріонетка у руках інших. Єдина його здатність - стріляти вогняними стрілами. Через свою вигідність його досі не вбили та регулярно лікують."],
                    7: ["Валіра Тимошенко", [1, -2, 1, 1, 2, 0], [-1, 1, 2, -2], [1, -1, 2, 1], [-1, -1, -1, 1, -2], [2, 1, 1, -2], [2, 1, -1, 0], [1, -1, -1, 1, 2], "Підступна розбійниця з луком та дуже гострими стрілами. Вона ні від кого не залежить і старається не приймати позиції жодних з політичних племен. Проте вона часто підпільно допомагає Андуіну. Та іншим, хто дасть більше голди."],
                    8: ["Ірель Меркель", [1, 1, 2, -1, -2, 0], [2, -2, 0, 1], [0, 1, -1, -1], [-2, 1, -1, 1, 1], [1, 2, -2, 1], [1, 1, -2, 2], [-1, -2, 1, 2, -1], "Жінка, яка найкраще знає, що таке влада та жертви. Ірель була скромною та надзвичайно доброю в дитинстві, але після війни її світогляди різко змінились. Вона стала суворою та владною, проте не втратила відчуття справедливості. Її основна сила - впливати на свідомість інших та розташовувати їх до себе. Єдиний персонаж, на руках якого немає крові. Або вона дуже ретельно її відмиває...."],
                    9: ["Громаш Кличко", [2, -1, 1, 1, -1, 1], [1, 1, 2, -1], [1, -2, 2, -2], [1, 2, 1, -2, 1], [-1, 2, -1, 2], [-1, 2, -1, 1], [2, 1, 1, -1, -1], "Найсміливіший та найсильніший богатир Залізної Орди. Про його мужність та відвагу знають усі. Його зброя - велика та дивовижна сокира. Проте не зважаючи на силу, Громаш через свою недалекість часто ледь не позбувався життя. "],
                    10: ["Малфуріон Ярош", [-1, 1, 1, -2, 0, 2], [1, -1, 1, 0], [1, 1, 1, -2], [-2, -1, 1, 1, -2], [1, -2, 2, -2], [1, -1, 2, -2], [-2, 1, 2, 2, 1], "Охоронець демонів та вірний слуга Кенарія. Володар рослинності та землі. Може з легкістю обезброїти противника, наславши на нього корені дерев. Найсильніший друїд світу."],
                    11: ["Джайна Клінтон", [2, -2, 1, 1, 0, -1], [-1, 1, 2, -2], [-2, -1, 2, -1], [1, 2, -1, 2, 2], [-1, -1, 1, 2], [1, -1, 1, 2], [1, 1, 2, 2, -2], "Наносити смертельні дози урону за допомогою льоду, вогню і таємної магії. Вона - одна із найсильніших, проте гарно вміє це приховувати. Така собі неофіційна володарка усього світу, спелів якої боїться кожен герой. "],
                    12: ["Рагнарос Лукашенко", [-1, 1, -2, 2, 1, 0], [-2, 2, -1, 1], [-1, 2, 1, -1], [1, 1, 2, -1, 1], [2, 1, -2, 1], [1, 2, -2, -1], [2, -1, 1, 1, 1], "Повелитель вогню та вулканів. Знищує усе на своєму шляху та ніколи не дивиться назад. Урон від вогняного м'яча здатен збити з ніг самого Рексара, але Рагнарос цього не робитиме. Рексар знає про нього одну велику таємницю, яка робить його повелителем."]
                }
            }
        }));
    }
});