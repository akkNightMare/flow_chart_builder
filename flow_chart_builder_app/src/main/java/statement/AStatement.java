/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statement;

/**
 *
 * @author Denis
 */
public abstract class AStatement {

    protected String statement;
    protected int quantityEndBracket = 0;
    private int begPozition = 0;

    public String getStatement() {
        return statement;
    }

    public int quantityEndBracket() {
        return quantityEndBracket;
    }

    public void setQuantityEndBracket(int quantityEndBracket) {
        this.quantityEndBracket = quantityEndBracket;
    }

    protected boolean haveEndBracket(String statement) {
        return statement.matches("(?s)^}.+");
    }

    protected int countEndBracket(String statement) {
        int count = 0;

        for (int i = 0; i < statement.length(); i++) {

            if (statement.charAt(i) == '\"'
                || statement.charAt(i) == '\'') {

                for (int j = i + 1; j < statement.length(); j++) {
                    if (statement.charAt(j) == statement.charAt(i)
                        && statement.charAt(j - 1) != '\\'
                        && statement.charAt(j - 2) != '\\') {
                        i = j;
                        break;
                    }
                }

            }

            if (statement.charAt(i) == '}') {
                count++;
                this.begPozition = i + 1;
            }

        }
        return count;
    }

    public AStatement(String statement) {
        if (haveEndBracket(statement)) {
            quantityEndBracket = countEndBracket(statement);
        }

        this.statement = statement.substring(this.begPozition).trim();
    }

}
