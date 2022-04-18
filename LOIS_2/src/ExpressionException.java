/////////////////////////////////////////////////////////////////////////////////////////
// Лабораторная работа №2 по дисциплине ЛОИС
// Вариант 6: Проверить равносильность формул
// Выполнена студентом группы 821703 БГУИР Шкут Роман Владимирович
// Класс предназначен для обеспечения прерывания работы при
// нахождении ошибки и передачи информации о типе найденной ошибки
//
// Код ЛОИС-1 авторства Шкута Романа Владимировича


public class ExpressionException extends Exception {
    private String message;
    private int error_number;

    public ExpressionException(int id) {
        this.error_number = id;
        this.message = get_error_message();
    }

    private String get_error_message(){
        switch (this.error_number) {
            case 1:
                return "Используется неизвестный символ";
            case 2:
                return "Нарушен синтаксис";
        }
        return "";
    }

    public int getError_number() {
        return error_number;
    }

    public String getMessage() {
        return message;
    }
}
