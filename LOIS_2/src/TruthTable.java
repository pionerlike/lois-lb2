/////////////////////////////////////////////////////////////////////////////////////////
// Лабораторная работа №2 по дисциплине ЛОИС
// Вариант 6: Проверить равносильность формул
// Выполнена студентом группы 821703 БГУИР Шкут Роман Владимирович
// Класс предназначен для формирования и заполнения таблицы истинност
//

public class TruthTable {
    private final int count_elements;
    private final int count_rows;
    private final int[][] table;

    public TruthTable(int n) {
        count_elements = n;
        count_rows = (int) Math.pow(2, n);
        table = new int[count_rows][n + 1];
        createTable(n);
    }

    private void createTable(int n) {
        for (int i = 0; i < count_rows; i++) {
            for (int j = n - 1; j >= 0; j--) {
                table[i][j] = (i / (int) Math.pow(2, j)) % 2;
            }
        }
    }
    public void print(){
        for (int i = 0; i < table.length; i++) {
            String temp = "";
            for (int j = 0; j < table[i].length; j++)
                temp += table[i][j];
            System.out.println(temp);
        }
    }


    public int equals(TruthTable equal_truth_table){
        int[][] equal_table = equal_truth_table.getTable();

        if(count_rows != equal_truth_table.getCountRows())
            return -1;
        for (int i = 0; i < count_rows; i++)
            if( table[i][count_elements] != equal_table[i][count_elements])
                return 1;
        return 0;
    }

    public int this_is_lie(){
        for (int i = 0; i < count_rows; i++)
            if( table[i][count_elements] != 0)
                return 1;
        return 0;
    }

    public int[][] getTable() {
        return table;
    }

    public int[] getRow(int i) {
        return table[i];
    }

    public int getValueRow(int i){
        return table[i][count_elements];
    }

    public int getCountRows() {
        return count_rows;
    }

    public void setValueRow(int i, boolean value) {
        table[i][count_elements] = value ? 1 : 0;
    }
}