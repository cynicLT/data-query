grammar Query;

operation : comparisonOperation
         | unionOperator '(' operatorList ')'
         | negationOperator '(' operation ')'
         ;

comparisonOperation : EQUAL '(' PROPERTY_STRING ',' STRING ')'
           | EQUAL '(' PROPERTY_NUMBER ',' NUMBER ')'
           | GREATER_THAN '(' PROPERTY_NUMBER ',' NUMBER ')'
           | LESS_THAN '(' PROPERTY_NUMBER ',' NUMBER ')'
           ;

unionOperator : 'AND' | 'OR' ;
negationOperator : 'NOT' ;

operatorList : operation (',' operation)* ;

EQUAL : 'EQUAL' ;
NOT : 'NOT' ;
GREATER_THAN : 'GREATER_THAN' ;
LESS_THAN : 'LESS_THAN' ;

PROPERTY_STRING : 'id' | 'title' | 'content' ;
PROPERTY_NUMBER : 'views' | 'timestamp' ;

STRING : '"' ~["]+ '"';

NUMBER : [0]|[1-9]+ ;

WS : [ \t\r\n]+ -> skip ;