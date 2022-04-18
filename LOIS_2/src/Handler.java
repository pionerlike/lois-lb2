/////////////////////////////////////////////////////////////////////////////////////////
// Лабораторная работа №2 по дисциплине ЛОИС
// Вариант 6: Проверить равносильность формул
// Выполнена студентом группы 821703 БГУИР Шкут Роман Владимирович
// Класс предназначен для обработки (проверки) формул, формирования и заполнения таблицы
// истинности, хранения информации о формуле
//
// Логические основы интеллектуальных систем. Практикум. Авторства В.В. Голоенков, В.П. Ивашенко,
// Д.Г. Колб, К.А. Уваров - БГУИР: 2011 г.
// Код ЛОИС-1 авторства Шкута Романа Владимировича


import java.util.*;

public class Handler {
    private final String verifiable_expression;
    private String out;
    private ExpressionTree tree;
    private final Set<String> atoms;
    private boolean correct = false;
    private TruthTable table;

    private static final List<String> symbols = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
    private static final List<String> add_symbols = new ArrayList<>(Arrays.asList("1", "0"));
    public static final List<String> all_signs = new ArrayList<>(Arrays.asList("!", "/\\", "\\/", "(", ")", "->", "~"));

    public Handler(String expression) {
        this.verifiable_expression = expression;
        atoms = new HashSet<>();
        out = "";
        try {
            check_used_symbols();
            check_brackets();
            tree = new ExpressionTree(expression, this);
            table = new TruthTable(atoms.size());
            out = "Это верная формула!";
            correct = true;
        } catch (ExpressionException ExpressionException) {
            out = "Это не верная формула!\n";
            out += ExpressionException.getMessage();
        }
    }

    private void check_used_symbols() throws ExpressionException {
        if (verifiable_expression.length() == 1)
            if (!(symbols.contains(verifiable_expression) || add_symbols.contains(verifiable_expression))) throw new ExpressionException(1);
        for (int i = 0; i < verifiable_expression.length(); i++) {
            if (!(symbols.contains("" + verifiable_expression.charAt(i)) || all_signs.contains("" + verifiable_expression.charAt(i))
                    || add_symbols.contains("" + verifiable_expression.charAt(i)))) {
                if (i + 1 < verifiable_expression.length()) {
                    String sign = "" + verifiable_expression.charAt(i) + verifiable_expression.charAt(i + 1);
                    if (all_signs.contains(sign))
                        i++;
                    else throw new ExpressionException(1);
                } else throw new ExpressionException(1);
            }
        }
    }

    private void check_brackets() throws ExpressionException {
        if (verifiable_expression.contains(")(") || verifiable_expression.charAt(0) == ')' ||
                (verifiable_expression.charAt(0) != '(' && verifiable_expression.length() != 1) ||
                (verifiable_expression.charAt(0) == '(' && verifiable_expression.charAt(verifiable_expression.length() - 1) != ')'))
            throw new ExpressionException(2);
        int open_bracket = 0, close_bracket = 0;
        for (int i = 0; i < verifiable_expression.length(); i++) {
            if (verifiable_expression.charAt(i) == '(') open_bracket++;
            else if (verifiable_expression.charAt(i) == ')') close_bracket++;
        }
        if (open_bracket != close_bracket)
            throw new ExpressionException(2);
    }


    public void create_truth_table() throws ExpressionException {
        TruthTable truthTable = new TruthTable(atoms.size());
        for (int i = 0; i < truthTable.getCountRows(); i++)
            truthTable.setValueRow(i, determine_value(truthTable.getRow(i), tree, new ArrayList<>(atoms)));
        this.table = truthTable;
    }


    private boolean determine_value(int[] value, ExpressionTree tree, ArrayList<String> list) throws ExpressionException {
        //System.out.println(tree.getOperation());
        switch (tree.getOperation()) {
            case "/\\":
                return determine_value(value, tree.getLeft(), list) & determine_value(value, tree.getRight(), list);
            case "\\/":
                return determine_value(value, tree.getLeft(), list) | determine_value(value, tree.getRight(), list);
            case "!":
                return !determine_value(value, tree.getLeft(), list);
            case "->":
                return !determine_value(value, tree.getLeft(), list) | determine_value(value, tree.getRight(), list);
            case "~":
                return (!determine_value(value, tree.getLeft(), list) & !determine_value(value, tree.getRight(), list)) |
                        (determine_value(value, tree.getLeft(), list) & determine_value(value, tree.getRight(), list));
            case "": {
                if ("1".equals(tree.getExpression()) || "0".equals(tree.getExpression()))
                    return "1".equals(tree.getExpression());
                return value[list.indexOf(tree.getExpression())] == 1;
            }
            default:
                throw new ExpressionException(2);
        }
    }

    public int getImpracticable() {
        return table.this_is_lie();
    }

    public int atoms_equal(Handler expression_2) {
        System.out.println(atoms);
        System.out.println(expression_2.getAtoms());
        Set<String> atoms_2 = expression_2.getAtoms();
        if (atoms.size() == atoms_2.size())
            return 0;
        for (String atom : atoms_2) {
            if (!atoms.contains(atom))
                return 0;
        }
        return 1;
    }

    public String getOut() {
        return out;
    }

    public Set<String> getAtoms() {
        return atoms;
    }

    public void addAtom(String atom) {
        atoms.add(atom);
    }

    public boolean getCorrect() {
        return correct;
    }

    public ExpressionTree getTree() {
        return tree;
    }

    public String getVerifiable_expression() {
        return verifiable_expression;
    }

    public void setTable(TruthTable table) {
        this.table = table;
    }

    public TruthTable getTable() {
        return this.table;
    }
}
