/////////////////////////////////////////////////////////////////////////////////////////
// Лабораторная работа №2 по дисциплине ЛОИС
// Вариант 6: Проверить равносильность формул
// Выполнена студентом группы 821703 БГУИР Шкут Роман Владимирович
// Класс предназначен для ведения диалога с пользователем и формирования формул
// необходимых для проверки равносильности
//
// Логические основы интеллектуальных систем. Практикум. Авторства В.В. Голоенков, В.П. Ивашенко,
// Д.Г. Колб, К.А. Уваров - БГУИР: 2011 г.

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dialog {
    private List<Handler> expressions;
    private List<Integer> selected_expressions;
    public int selected_count;

    public Dialog() {
        this.selected_count = 0;
        this.expressions = new ArrayList<>();
        this.selected_expressions = new ArrayList<>();
    }

    public void run(int count) throws ExpressionException {
        if (count < 2 || count > expressions.size())
            this.selected_count = 2;
        else
            this.selected_count = count;
        try {
            choose_list();
            for (int number : selected_expressions) {
                expressions.get(number).create_truth_table();
                System.out.println("\n----------------------------");
                System.out.println("Таблица истинности для формулы\n" + expressions.get(number).getVerifiable_expression());
                System.out.println("----------------------------");
                expressions.get(number).getTable().print();
                System.out.println("----------------------------\n");
            }

            List<Handler> inversion_expressions = new ArrayList<>();
            for (int i = 0; i < selected_expressions.size(); i++)
                inversion_expressions.add(new Handler("(!" + expressions.get(selected_expressions.get(i)).getVerifiable_expression() + ")"));
            for (int i = 0; i < selected_expressions.size(); i++) {
                for (int j = i; j < selected_expressions.size(); j++) {
                    String out = "";
                    if(!expressions.get(selected_expressions.get(i)).equals(expressions.get(selected_expressions.get(j)))){
                    if(expressions.get(selected_expressions.get(i)).atoms_equal(expressions.get(selected_expressions.get(j))) == 1){
                        out = "формулы не равносильны - различные атомы";
                    }
                    else {

                        Handler temp_1 = new Handler("(" + inversion_expressions.get(i).getVerifiable_expression()
                                + "/\\" + expressions.get(selected_expressions.get(j)).getVerifiable_expression() + ")");
                        Handler temp_2 = new Handler("(" + inversion_expressions.get(j).getVerifiable_expression()
                                + "/\\" + expressions.get(selected_expressions.get(i)).getVerifiable_expression() + ")");
                        temp_1.create_truth_table();
                        temp_2.create_truth_table();

                        if (temp_1.getImpracticable() == 0 && temp_2.getImpracticable() == 0)
                            out = "формулы равносильны";
                        else out = "формулы не равносильны";
                    }

                    System.out.println("\n\n" + expressions.get(selected_expressions.get(i)).getVerifiable_expression()
                            + "\n" + expressions.get(selected_expressions.get(j)).getVerifiable_expression()
                            + "\nРезультат - " + out);
                    }

                }
            }
        } catch (ExpressionException e) {
            System.out.println("Ошибка - " + e);
        }
    }

    private void choose_list(){
        System.out.println("\n----------------------------");
        System.out.println("Диалог запущен");
        System.out.println("----------------------------");
        for (int j = 0; j < expressions.size(); j++)
            System.out.println((j + 1) + " -  " + expressions.get(j).getVerifiable_expression());
        System.out.println("----------------------------\n");

        for(int i = 0; i < selected_count; i++) {
            boolean check = true;
            while (check) {
                System.out.println("Выберете формулу №" + (i + 1));
                Scanner in = new Scanner(System.in);
                int temp = in.nextInt();
                if (temp > expressions.size() || temp < 0) {
                    System.out.println("Неправильный ввод");
                    continue;
                }
                for (int num : selected_expressions)
                    if (num == temp - 1) {
                        System.out.println("Неправильный ввод");
                        continue;
                    }
                selected_expressions.add(temp - 1);
                check = false;
            }
        }
    }

    public void addExpressions(Handler expression){
        expressions.add(expression);
    }
}
