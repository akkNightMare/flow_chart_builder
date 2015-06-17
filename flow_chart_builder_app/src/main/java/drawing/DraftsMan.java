/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

import exeptions.InvalidQuantityBracketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import parsing.Method;
import parsing.MethodParser;
import parsing.ParsedClass;
import statement.*;

/**
 *
 * @author Denis
 */
public class DraftsMan {

    private final ParsedClass parsedClass;
    private final Map<String, String> methodGraphs = new HashMap<String, String>();
    private final StringBuilder graphForClassMembers = new StringBuilder();
    private String previousNode = "";
    private String previousEdge = "";
    private static final String NO_METHODS_MESSAGE = "There is no method in the class";
    private static final String NO_FIELDS_MESSAGE = "There is no field in the class";
    private final String className;

    public DraftsMan(ParsedClass parsedClass) {
        className = parsedClass.getClassName();
        this.parsedClass = parsedClass;
    }

    private void resetPreviousNodeAndEdge() {
        this.previousEdge = "";
        this.previousNode = "";
    }

    public String getGraphForClassMembers() {
        return graphForClassMembersIsBlank() ? buildGraphForClass() : graphForClassMembers.toString();
    }

    public String getClassName() {
        return className;
    }

    private boolean graphForClassMembersIsBlank() {
        return "".equals(graphForClassMembers.toString());
    }

    private String startGraph(String graphName) {
        return "digraph \"" + graphName + "\" {\n";
    }

    private String startGraph(String graphName, String attributes) {
        return "digraph \"" + graphName + "\" {\n" + attributes + "\n";
    }

    private String addStartBracket() {
        return "{";
    }

    private String addEndBracket() {
        return "}\n";
    }

    private String startCluster(String name, String label, String attributes) {
        return "\nsubgraph cluster_" + name + " {\nlabel=\"" + label + "\"\n" + attributes + "\n";
    }

    private String startCluster(String name, String label) {
        return "\nsubgraph cluster_" + name + " {\nlabel=\"" + label + "\"\n";
    }

    private boolean isBlank(StringBuilder sb) {
        return "".equals(sb.toString());
    }

    private List<String> getFields() {
        return parsedClass.getFields();
    }

    private String addNodes(List<String> nodes) {
        StringBuilder sb = new StringBuilder();

        for (String node : nodes) {
            sb.append("\"").append(node).append("\";\n");
        }

        previousNode = nodes.get(nodes.size() - 1);
        return sb.toString();
    }

    private String addNode(String node, String attr) {
        previousNode = node;
        return "\"" + node + "\"" + (attr != null ? attr : "") + ";\n";
    }

    private String startBracket() {
        return "{\n";
    }

    private String buildGraphForClass() {
        final List<String> fields = getFields();
        final List<String> methods = getMethodNames();

        if (fields.isEmpty()) {
            fields.add(NO_FIELDS_MESSAGE);
        }

        if (methods.isEmpty()) {
            methods.add(NO_METHODS_MESSAGE);
        }

        graphForClassMembers.append(
                startGraph(parsedClass.getClassName(),
                           "node[shape=plaintext width=5.5 ] edge[style=invis] fontcolor=blue ranksep=0.1")
        );

        //Start class cluster
        graphForClassMembers.append(
                startCluster("class_name", parsedClass.getClassName(), "style=rounded fontsize=22")
        );

        //Class Fields Cluster
        graphForClassMembers.append(
                startCluster("class_fields", "Class Fields:",
                             "node[fontcolor=forestgreen] style=solid fontsize=17 fontcolor=darkorchid fontname=\"times-italic\"")
        );

        graphForClassMembers.append(addNodes(fields));
        graphForClassMembers.append(addEndBracket());

        //Class Methods Cluster
        graphForClassMembers.append(
                startCluster("class_methods", "Class Methods:",
                             "node[fontcolor=coral] style=solid fontsize=17 fontcolor=darkorchid fontname=\"times-italic\"")
        );

        for (String method : methods) {
            graphForClassMembers.append(addNode(method, "[URL=\"d:\\img\\" + method.replaceAll("<|>", " ") + ".svg\"]"));
        }
        graphForClassMembers.append(addEndBracket());

        //Add Edges
        graphForClassMembers.append(addEdges(fields));
        graphForClassMembers.append(addEdges(methods));

        //Inner Classes Cluster
        if (parsedClass.getReference() != null) {
            String innerClassName = parsedClass.getReference().getClassName();
            graphForClassMembers.append(startCluster("inner_classes", "Inner Classes:",
                                                     "node[fontcolor=cornflowerblue] style=solid fontsize=17 fontcolor=darkorchid fontname=\"times-italic\"")
            );
            graphForClassMembers.append(addNode(innerClassName, null));
            graphForClassMembers.append(addEndBracket());
            graphForClassMembers.append(addEdge(innerClassName, null));
        }

        graphForClassMembers.append(addEndBracket()); //End class cluster
        graphForClassMembers.append(addEndBracket()); //End graph
        resetPreviousNodeAndEdge();
        return this.graphForClassMembers.toString();
    }

    public Map<String, String> buildGraphsForMethods() throws InvalidQuantityBracketException {

        for (Method method : parsedClass.getMethods()) {
            StringBuilder graph = new StringBuilder();
            String methodName = method.getName();
            List<AStatement> statements = new ArrayList<>();
            statements = MethodParser.parse(method.getBody());

            //Makes graph
            graph.append(startGraph(methodName, "node[shape=box] splines=ortho forcelabels=true"));
            graph.append(startCluster("method", methodName, "fontsize=17 style=dotted fontcolor=coral fontname=\"times-italic\""));

            //Get Nodes
            List<String> nodes = new ArrayList<>();
            for (AStatement statement : statements) {
                String node = "";

                if (!statement.getClass().equals(ElseStatement.class)) {

                    if (statement.getClass().equals(StartStatement.class)
                        || statement.getClass().equals(EndStatement.class)) {

                        node = addNode(statement.getStatement(), "[shape=octagon]");

                    } else if (!statement.getClass().equals(SimpleStatement.class)) {

                        if (statement.getClass().equals(ForStatement.class)) {
                            node = addNode(statement.getStatement(), "[shape=hexagon]");
                        } else if (statement.getClass().equals(IfStatement.class) || statement.getClass().equals(ElseIfStatement.class)) {
                            node = addNode(statement.getStatement(), "[shape=diamond]");
                        } else if (statement.getClass().equals(TryStatement.class)) {
                            node = addNode(statement.getStatement(), "[shape=invhouse]");
                        }

                    } else {
                        node = addNode(statement.getStatement(), null);
                    }

                    nodes.add(node);
                }
            }

            //Get Edges
            List<String> edges = new ArrayList<>();
            Stack<AStatement> blockStatements = new Stack<>();
            String edgeLabel = "";
            for (AStatement statement : statements) {
                String edge = "";

                if (isElseStatement(statement)) {
                    String str = blockStatements.pop().getStatement(); //Get "if" from stack
                    blockStatements.push(new SimpleStatement(previousEdge));
                    this.previousEdge = str;
                    continue;
                }

                if (statementHasEndBracket(statement)) {
                    if (statement.quantityEndBracket() > blockStatements.size()) {
                        throw new InvalidQuantityBracketException("[buildGraphForMethod]: Detected invalid quantity bracket in class! ->" + statement.getStatement());
                    }
                    for (int i = 0; i < statement.quantityEndBracket(); i++) {

                        if (isForStatement(blockStatements.peek())) {
                            edge = addEdgeWithCustomPreviousNode(statement.getStatement(), this.previousEdge, "[style=invis]") + addEdge(blockStatements.pop().getStatement(), null);
                            edgeLabel = "[xlabel=false fontcolor=red color=crimson]";
                        } else {
                            edge = "\"" + blockStatements.pop().getStatement() + "\" -> " + addNode(statement.getStatement(), "[xlabel=false fontcolor=red color=crimson]");
                        }

                        edges.add(edge);
                    }
                }

                if (!statement.getClass().equals(SimpleStatement.class)
                    && !statement.getClass().equals(StartStatement.class)
                    && !statement.getClass().equals(EndStatement.class)) {

                    blockStatements.add(statement);
                    edge = addEdge(statement.getStatement(), edgeLabel);
                    edgeLabel = "[xlabel=true fontcolor=green color=darkgreen]";

                } else {
                    edge = addEdge(statement.getStatement(), edgeLabel);
                    edgeLabel = "";
                }

                edges.add(edge);
            }

            for (String node : nodes) {
                graph.append(node);
            }
            for (String edge : edges) {
                graph.append(edge);
            }

            graph.append(addEndBracket()); //End cluster
            graph.append(addEndBracket()); //End graph
            resetPreviousNodeAndEdge();
            this.methodGraphs.put(methodName, graph.toString());

        }

        return this.methodGraphs;
    }

    private boolean isForStatement(AStatement statement) {
        return statement.getClass().equals(ForStatement.class);
    }

    private boolean isElseStatement(AStatement statement) {
        return statement.getClass().equals(ElseStatement.class);
    }

    private boolean statementHasEndBracket(AStatement statement) {
        return statement.quantityEndBracket() > 0;
    }

    private List<String> getMethodNames() {
        List<Method> methods = parsedClass.getMethods();
        List<String> names = new ArrayList<>();

        for (Method method : methods) {
            names.add(method.getName());
        }

        return names;
    }

    private String addEdges(List<String> edges) {
        StringBuilder sb = new StringBuilder("\n");

        if (!"".equals(previousEdge)) {
            sb.append("\"").append(previousEdge).append("\"").append(" -> ");
        }

        for (int i = 0; i < edges.size(); i++) {
            if (i == edges.size() - 1) {
                sb.append("\"").append(edges.get(i)).append("\"").append(";\n");
                previousEdge = edges.get(i);
            } else {
                sb.append("\"").append(edges.get(i)).append("\"").append(" -> ");
            }
        }

        return sb.toString();
    }

    private String addEdge(String edge, String attr) {
        StringBuilder sb = new StringBuilder();

        if (!"".equals(previousEdge)) {
            sb.append("\"").append(previousEdge).append("\"").append(" -> ");
        } else {
            this.previousEdge = edge;
            return "";
        }

        sb.append("\"").append(edge).append("\"").append((attr != null ? attr : "")).append(";\n");
        previousEdge = edge;
        return sb.toString();
    }

    private String addEdgeWithCustomPreviousNode(String node, String previousNode, String attr) {
        String str = attr != null ? attr : "";
        return "\"" + previousNode + "\" -> \"" + node + "\"" + str + ";\n";
    }

}
