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
public class StatementsFactory {

    private final String START_STR = "(?s)(}\\s*)*";

    private StatementsFactory() {
    }

    public static StatementsFactory getInstance() {
        return StatementsFactoryHolder.INSTANCE;
    }

    private static class StatementsFactoryHolder {

        private static final StatementsFactory INSTANCE = new StatementsFactory();
    }

    public AStatement makeStatement(String statement) {
        if (isForStatement(statement)) {
            return new ForStatement(statement);
        } else if (isIfStatement(statement)) {
            return new IfStatement(statement);
        } else if (isElseIfStatement(statement)) {
            return new ElseIfStatement(statement);
        } else if (isElseStatement(statement)) {
            return new ElseStatement(statement);
        } else if (isWhileStatement(statement)) {
            return new WhileStatement(statement);
        } else if (isEndStatement(statement)) {
            return new EndStatement(statement);
        } else if (isStartStatement(statement)) {
            return new StartStatement(statement);
        } else if (isTryStatement(statement)) {
            return new TryStatement(statement);
        } else if (!"".equals(statement)) {
            return new SimpleStatement(statement);
        }

        return null;
    }

    private boolean isTryStatement(String statement) {
        return statement.matches(START_STR + "(try)");
    }

    private boolean isForStatement(String statement) {
        return statement.matches(START_STR + "for\\s*\\(.+\\)");
    }

    private boolean isIfStatement(String statement) {
        return statement.matches(START_STR + "if\\s*\\(.+\\)");
    }

    private boolean isElseIfStatement(String statement) {
        return statement.matches(START_STR + "else\\s+if\\s*\\(.+\\)");
    }

    private boolean isElseStatement(String statement) {
        return statement.matches(START_STR + "(else)");
    }

    private boolean isWhileStatement(String statement) {
        return statement.matches(START_STR + "while\\s*\\(.+\\)");
    }

    private boolean isEndStatement(String statement) {
        return statement.matches(START_STR + "(\\$end\\$)");
    }

    private boolean isStartStatement(String statement) {
        return statement.matches(START_STR + "(\\$start\\$)");
    }
}
