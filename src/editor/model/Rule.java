package editor.model;

import java.util.ArrayList;

/**
 * Created by vilddjur on 1/28/17.
 */
public class Rule {
    public Pattern matchingPattern;
    public ArrayList<Pattern> possibleTranslations;
    public Rule(Pattern matchingPattern){
        this.matchingPattern = matchingPattern;
        possibleTranslations = new ArrayList<Pattern>();
    }
}
