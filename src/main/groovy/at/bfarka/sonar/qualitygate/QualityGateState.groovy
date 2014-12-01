package at.bfarka.sonar.qualitygate

/**
 * Created by berndfarka on 25.11.14.
 */
enum QualityGateState {
    OK('OK',1), WARNING('WARN',2),ERROR('ERROR',3), NO_RESULT("NORESULT",4)


    private final stringValue
    private final int intValue

    private QualityGateState(def stringValue, int intValue){
        this.stringValue = stringValue
        this.intValue = intValue
    }

    public static QualityGateState fromString(def value){
        return this.values().findResult(NO_RESULT, { it.stringValue.equals(value) ? it : null})
    }

    public String getStringValue(){
        return stringValue
    }

    public int intValue(){
        return intValue;
    }
}