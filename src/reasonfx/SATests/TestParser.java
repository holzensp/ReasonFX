/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reasonfx.SATests;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

import reasonfx.parsers.LogicParser;
import reasonfx.rule.Given;
import reasonfx.rule.Rule;
import reasonfx.rule.RuleInstance;
import reasonfx.term.Term;
import reasonfx.rule.UnificationException;
import reasonfx.rule.Wanted;
import reasonfx.util.ReasonLogger;

/**
 *
 * @author holzensp
 */
public class TestParser extends Application {
    public static final ReasonLogger LOGGER = new ReasonLogger(TestParser.class);
    
    private static final Term[][] tests;
    private static final String[] results;
    private static final Rule andEL = LogicParser.parse(LogicParser::dedrule,"@x^@y |- @x").r;
    private static final RuleInstance rs[] = {
        RuleInstance.instantiate(andEL),
        RuleInstance.instantiate(andEL),
        RuleInstance.instantiate(andEL),
        RuleInstance.instantiate(andEL)};
    
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
                Given g = new Given(Objects.requireNonNull(tests[i][1]));
                tests[i][0].unify(g, tests[i][1]);
                results[i] = "Yes";
            } catch (UnificationException ex) {
                Logger.getLogger(TestParser.class.getName()).log(Level.FINE, null, ex);
                results[i] = "No";
            }
            i++;
        }
    }
    
    public void reportRuleInstances(String msg) {
        LOGGER.info("{0}:  {1}  |  {2}  |  {3}  | {4}  ", msg, rs[0], rs[1], rs[2], rs[3]);
    }
    
    @Override
    public void start(Stage primaryStage) {
        System.out.println("\n\n\n");
        
        LOGGER.info(andEL.toString());
        
        rs[3].renumber(rs[2].renumber(rs[1].renumber(rs[0].renumber(0))));

        reportRuleInstances("INIT");
        LOGGER.info("Unifying conclusion of rule 0 with wanted of rule 1");
        rs[0].unify(new Wanted(rs[1].getPremisses().iterator().next()));
        LOGGER.info("Unifying conclusion of rule 0 with wanted of rule 1 AGAIN");
        rs[0].unify(new Wanted(rs[1].getPremisses().iterator().next()));
        reportRuleInstances("1->2");
        rs[2].unify(new Wanted(rs[0].getPremisses().iterator().next()));
        reportRuleInstances("3->1");
        rs[3].unify(new Wanted(rs[2].getPremisses().iterator().next()));
        reportRuleInstances("4->3");
        rs[2].disconnect();
        reportRuleInstances("3/>1");
        
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(new String[0]);
    }
    
    
}
