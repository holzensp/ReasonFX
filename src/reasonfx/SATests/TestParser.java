/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.SATests;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import reasonfx.parsers.LogicParser;
import reasonfx.rule.GivenImpl;
import reasonfx.rule.Rule;
import reasonfx.rule.RuleInstance;
import reasonfx.rule.RuleInstanceVariable;
import reasonfx.rule.Term;
import reasonfx.rule.UnificationException;
import reasonfx.rule.Unifier;

/**
 *
 * @author holzensp
 */
public class TestParser extends Application {
    static String inp = "UNDEFINED";
    private static final Term[][] tests;
    private static final String[] results;
    private static final Rule andEL = LogicParser.parse(LogicParser::dedrule,"@x^@y |- @x").r;
    private static final RuleInstance rs[] = {
        RuleInstance.instantiate(andEL),
        RuleInstance.instantiate(andEL),
        RuleInstance.instantiate(andEL) };
    
    static {
        String[][] inps =
            { {"p","p"}
            , {"p","q"}
            , {"a^b","a & b"}
            , {"!p","!p"}
            , {"x^(y|z)", "x^(y|(!z))"}
            , {"x^(y|!z)", "x^(y|(!z))"}
            , {"|- @x","p"}
            , {"|- @x ^ @x", "p^p" }
            , {"|- @x ^ @x", "p^q" }
            , {"|- @x ^ @y", "p^p" }
            , {"|- @x ^ @y", "p^q" }
            , {"!p","!p"}
            };
        tests = new Term[inps.length][2];
        results = new String[inps.length];
        
        int i = 0;
        for(String[] pair : inps) {
            int j = 0;
            for(String in : pair) {
                System.err.println(String.format("(%d,%d) %s", i, j, in));
                if(in.contains("@")) {
                    tests[i][j] = RuleInstance.instantiate(LogicParser.parse(LogicParser::dedrule,pair[j]).r).getConclusion();
                } else {
                    tests[i][j] = LogicParser.parse(LogicParser::proposition,pair[j]).t;
                }
                j++;
            }
            try {
                GivenImpl g = new GivenImpl(Objects.requireNonNull(tests[i][1]));
                tests[i][0].unify(g, tests[i][1]);
                results[i] = "Yes, with " + new Unifier(g.getBoundVariables());
            } catch (UnificationException ex) {
                Logger.getLogger(TestParser.class.getName()).log(Level.FINE, null, ex);
                results[i] = "false";
            }
            i++;
        }
    }
    
    public void reportRuleInstances(String msg) {
        System.out.println(
            String.format(msg + ":  %s  |  %s  |  %s  ", rs[0], rs[1], rs[2])
        );
        Stream.concat(
            rs[0].collect(RuleInstanceVariable.class),
          Stream.concat(
            rs[1].collect(RuleInstanceVariable.class),
            rs[2].collect(RuleInstanceVariable.class)
          )
        ).distinct().filter(RuleInstanceVariable::isBound).forEach(v -> System.out.println(" " + v.dbgString()));
    }
    
    @Override
    public void start(Stage primaryStage) {
        FlowPane ins = new FlowPane(Orientation.VERTICAL);
        FlowPane outs = new FlowPane(Orientation.VERTICAL);
        for(Term[] pair : tests) {
            ins.getChildren().add(new Label(
                pair[0].show() + " ~ " + pair[1].show()));
        }
        for(String res : results) {
            outs.getChildren().add(new Label("" + res));
        }

        FlowPane root = new FlowPane(Orientation.HORIZONTAL);
        root.getChildren().addAll(ins,outs);
        
        Scene scene = new Scene(root, 900, 250);
        
        scene.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            primaryStage.close();
        });
        
        System.out.println(andEL.dbgString());
        
        reportRuleInstances("INIT");
        rs[0].unify(() -> rs[1].getPremisses().iterator().next());
        rs[1].renumber(rs[0].renumber(0));
        reportRuleInstances("1->2");
        rs[1].unify(() -> rs[2].getPremisses().iterator().next());
        rs[2].renumber(rs[1].renumber(rs[0].renumber(0)));
        reportRuleInstances("2->3");
        rs[0].disconnect();
        reportRuleInstances("1/>2");
        
        primaryStage.setTitle("First Order Logic Parser Result");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(new String[0]);
    }
    
    public static <T> T callParser(Supplier<T> s, Class<T> c) {
        return s.get();
    }
    
}
