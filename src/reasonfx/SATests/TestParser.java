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
import reasonfx.rule.Term;
import reasonfx.rule.UnificationException;
import reasonfx.rule.Unifier;
import reasonfx.rule.Wanted;

/**
 *
 * @author holzensp
 */
public class TestParser extends Application {
    static String inp = "UNDEFINED";
    private static final Term[][] tests;
    private static final String[] results;
    
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
                    tests[i][j] = new RuleInstance(LogicParser.parse(LogicParser::dedrule,pair[j]).r).getConclusion();
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
        
        Rule r = LogicParser.parse(LogicParser::dedrule,"@x^@y |- @x").r;
        System.out.println(r.toString());
        RuleInstance r1 = new RuleInstance(r), r2 = new RuleInstance(r), r3 = new RuleInstance(r);
        
        System.out.println(String.format("INIT:  %s  |  %s  |  %s  ", r1, r2, r3));
        r1.unify(() -> r2.getPremisses().iterator().next());
        System.out.println(String.format("1->2:  %s  |  %s  |  %s  ", r1, r2, r3));
        r2.unify(() -> r3.getPremisses().iterator().next());
        System.out.println(String.format("2->3:  %s  |  %s  |  %s  ", r1, r2, r3));
        r1.disconnect();
        System.out.println(String.format("1/>2:  %s  |  %s  |  %s  ", r1, r2, r3));
        
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
