/////////////////////////////////////////////////////////////////////////////////////////
// Лабораторная работа №2 по дисциплине ЛОИС
// Вариант 6: Проверить равносильность формул
// Выполнена студентом группы 821703 БГУИР Шкут Роман Владимирович
// Класс предназначен для преобразования формулы в дерево выражений (состоящее из
// подформул, их атомов и операций)
//
// Код ЛОИС-1 авторства Шкута Романа Владимировича

public class ExpressionTree {
    private String expression;
    private String operation;
    private ExpressionTree left;
    private ExpressionTree right;
    private final Handler root;

    public ExpressionTree(String expression, Handler root) throws ExpressionException {
        this.expression = expression;
        this.root = root;
        operation = "";

        try {
            withoutBrackets();
        } catch (ExpressionException expressionException) {
            throw new ExpressionException(expressionException.getError_number());
        }

    }

    private void withoutBrackets() throws ExpressionException {
        if (expression.length() == 0)
            throw new ExpressionException(2);
        if (expression.length() == 1) {
            left = right = null;
            if (!"1".equals(expression) && !"0".equals(expression))
                    root.addAtom(expression);
            return;
        }
        if (expression.charAt(0) == '!') {
            this.operation = "!";
            right = null;
            left = new ExpressionTree(copy(expression, 1, expression.length()), root);
            return;
        }
        if (expression.charAt(0) == '(' && expression.charAt(expression.length() - 1) == ')') {
            String expression = copy(this.expression, 1, this.expression.length() - 1);
            if(expression.length() == 1)
                throw new ExpressionException(2);
            int pointerSign = search_sign_outside_brackets(expression);
            if (pointerSign == 0) {
                if(expression.charAt(pointerSign) != '!'){
                    throw new ExpressionException(2);
                }
                right = null;
                left = new ExpressionTree(copy(expression, 1, expression.length()), root);
                operation = search_sign(expression, pointerSign);
                return;
            }
            if (pointerSign == -1) {
                throw new ExpressionException(2);
            }
            operation = search_sign(expression, pointerSign);
            String leftExpression = copy(expression, 0, pointerSign);
            if (operation.length() == 2) {
                pointerSign += 1;
            }
            String rightExpression = copy(expression, pointerSign + 1, expression.length());
            left = new ExpressionTree(leftExpression, root);
            right = new ExpressionTree(rightExpression, root);
        } else {
            throw new ExpressionException(2);
        }
    }

    private String copy(String expression, int start, int end) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < end; i++)
            stringBuilder.append(expression.charAt(i));
        return stringBuilder.toString();
    }

    private int search_sign_outside_brackets(String expression) {
        int check = 0;
        for (int i = 0; i < expression.length(); i++) {
            if ((expression.charAt(i) != '(' && expression.charAt(i) != ')') && check == 0) {
                String sign = search_sign(expression, i);
                if (Handler.all_signs.contains(sign)) {
                    return i;
                }
            }
            if (expression.charAt(i) == '(') {
                check++;
            } else if (expression.charAt(i) == ')') {
                check--;
            }
        }
        return -1;
    }

    private String search_sign(String expression, int pointer) {
        if (expression.charAt(pointer) == '!' || expression.charAt(pointer) == '~')
            return expression.charAt(pointer) + "";
        return "" + expression.charAt(pointer) + expression.charAt(pointer + 1);
    }

    public String getExpression() {
        return expression;
    }

    public ExpressionTree getLeft() {
        return left;
    }

    public ExpressionTree getRight() {
        return right;
    }

    public String getOperation() {
        return operation;
    }
}