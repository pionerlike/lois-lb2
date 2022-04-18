/////////////////////////////////////////////////////////////////////////////////////////
// Лабораторная работа №2 по дисциплине ЛОИС
// Вариант 6: Проверить равносильность формул
// Выполнена студентом группы 821703 БГУИР Шкут Роман Владимирович
// Класс предназначен для поиска формул, их передачи в класс Handler, вывода результата
// их обработки с дальнейшей возможной передачей в класс Dialog, запуска диалога.
//
// Код ЛОИС-1 авторства Шкута Романа Владимировича

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final List<String> FILE_NAMES = new ArrayList<>(Arrays.asList("1.txt", "2.txt", "3.txt","4.txt", "5.txt"));
    private static final String FILE_PATH = System.getProperty("user.dir") + "/tests/";
    public static final int selected_count = 2;

    public static void main(String[] args) throws IOException, ExpressionException {
        Dialog dialog = new Dialog();
        for (int i = 0; i < FILE_NAMES.size(); i++) {
            String expression = "";
            StringBuilder builder = new StringBuilder(expression);
            try (FileInputStream fileIn = new FileInputStream(FILE_PATH + FILE_NAMES.get(i))) {
                while (fileIn.available() > 0)
                    builder.append((char) fileIn.read());
                expression = builder.toString();
            } catch (FileNotFoundException ex) {
                System.out.println("Файл не найден!!!");
            }
            if(expression.length() > 0) {
                Handler handler = new Handler(expression);
                System.out.println('\n' + expression + '\n' + handler.getOut());
                if (handler.getCorrect())
                    dialog.addExpressions(handler);
            } else System.out.println("\nПустая строка!");

        }
        dialog.run(selected_count);
    }
}
