package org.cynic.data_query.framework.antlr;

import java.util.Map;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.cynic.data_query.domain.ApplicationException;

public class CustomAntlrErrorListener extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new ApplicationException("error.antlr.parse", e, Map.entry("message", msg));
    }
}
