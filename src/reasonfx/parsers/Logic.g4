grammar Logic;

options{
	language=Java;
}

@header {
    import reasonfx.rule.*;
    import reasonfx.term.*;

    import java.util.function.Function;
    import java.util.Objects;
    import java.util.TreeMap;
    import java.util.Map;
    import java.util.Arrays;
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

@parser::members {
    private static final Operator JOP_NOT, JOP_AND, JOP_OR, JOP_XOR, JOP_IMPLIES;
    static {
        try {
            JOP_NOT     = Operator.getInstance("\u00AC", 1, false, 10);
            JOP_AND     = Operator.getInstance("\u2227", 2, true,   9);
            JOP_OR      = Operator.getInstance("\u2228", 2, true,   8);
            JOP_XOR     = Operator.getInstance("\u22BB", 2, true,   8);
            JOP_IMPLIES = Operator.getInstance("\u2283", 2, false,  7);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Map<String,RuleVariable> rvs = new TreeMap();
    public static boolean parseRules = false;

    private Term mkSentence(Operator operator, Term... arguments) {
        Operator   op = requireNonNull(operator);
        List<Term> as = Arrays.asList(arguments);
        as.stream().forEach(LogicParser::requireNonNull);
        try {
            return new Sentence(op, as);
        } catch(Exception e) {
            throw new ParseCancellationException(e);
        }
    }

    private static <T> T requireNonNull(T o) {
        try {
            return Objects.requireNonNull(o, "Parser produced null value");
        } catch(NullPointerException e) {
            throw new ParseCancellationException(e);
        }
    }

    public static <Result> Result parse(Function<LogicParser,Result> parser, String input) {
        CommonTokenStream str = new CommonTokenStream(
            new LogicLexer(
                new ANTLRInputStream(input)
            ));
        LogicParser p = new LogicParser(str);
        rvs.clear();
        return parser.apply(p);
    }

    public static RuleVariable mkRuleVariable(String name) {
        if(!rvs.containsKey(name)) {
            RuleVariable result = new RuleVariable(rvs.size());
            rvs.put(name,result);
        }

        return rvs.get(name);
    }
}

dedrule returns [Rule r]
@init{LogicParser.parseRules = true; rvs.clear();}
    : entailment EOF {$r = new Rule($entailment.e);}
    ;

entailment returns [Entailment e]
@init{List<Term> lst = new ArrayList(); }
    : (p=formula {lst.add($p.t);}
       (COMMA ps=formula {lst.add($ps.t);})*)?
      OP_ENTAILS c=formula  {$e = new EntailmentBase(
            requireNonNull($c.t),
            requireNonNull(lst.toArray(new Term[lst.size()]))
        ); }
      EOF
    ;

proposition returns [Term t]
@init{LogicParser.parseRules = false;}
    : f=formula EOF  {$t = requireNonNull($f.t);}
    ;



formula returns [Term t]
    : OP_NOT arg=formula                 {$t = mkSentence(JOP_NOT,     $arg.t);}
    | lhs=formula OP_AND     rhs=formula {$t = mkSentence(JOP_AND,     $lhs.t, $rhs.t);}
    | lhs=formula OP_OR      rhs=formula {$t = mkSentence(JOP_OR,      $lhs.t, $rhs.t);}
    | lhs=formula OP_XOR     rhs=formula {$t = mkSentence(JOP_XOR,     $lhs.t, $rhs.t);}
    | lhs=formula OP_IMPLIES rhs=formula {$t = mkSentence(JOP_IMPLIES, $lhs.t, $rhs.t);}
    | '(' in=formula ')'                 {$t = requireNonNull($in.t);}
    | var                                {$t = requireNonNull($var.v);}
    ;

var returns [Variable v]
    : {!parseRules}? ELEMPROP     {$v = ElemProp.getInstance($ELEMPROP.text);}
    | { parseRules}? RULEVAR      {$v = mkRuleVariable($RULEVAR.text);}
    ;


/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

OP_NOT:       ('!');
OP_AND:       ('&' | '^');
OP_OR:        ('|' | '\\' '/' );
OP_XOR:       ('\\' '_' '/' | '<' '/' '>');
OP_IMPLIES:   ('-' '>');
OP_ENTAILS:   ('|' '-');
ELEMPROP:     LOWER CHARACTER*;
RULEVAR:      '@' CHARACTER*;
WHITESPACE:   [ \t\r\n]+ -> skip;
COMMA:        (',');

fragment LOWER: ('a'..'z');
fragment UPPER: ('A'..'Z');
fragment DIGIT: ('0'..'9');
fragment CHARACTER: (DIGIT | LOWER | UPPER | '_');



/*
condition: formula EOF ;

formula	
	:	((FORALL | EXISTS) VARIABLE)? disjunction ;
predicate 
	:	PREPOSITION predicateTuple -> ^(PREDICATE PREPOSITION predicateTuple)
	| 	PREPOSITION ;
predicateTuple
	:	LPAREN! term (','! term)* RPAREN! ;
term	:	function | VARIABLE ;
function:	CONSTANT functionTuple -> ^(FUNCTION CONSTANT functionTuple)
	|	CONSTANT;
functionTuple
	:	LPAREN! (CONSTANT | VARIABLE) (','! (CONSTANT | VARIABLE) )* RPAREN!;
*/
