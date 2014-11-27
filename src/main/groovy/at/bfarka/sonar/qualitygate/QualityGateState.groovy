package at.bfarka.sonar.qualitygate

/**
 * Created by berndfarka on 25.11.14.
 */
enum QualityGateState {
    OK('OK'), WARNING('WARN'),ERROR('ERROR'), NO_RESULT("NORESULT")


    private final stringValue

    private QualityGateState(def stringValue){
        this.stringValue = stringValue
    }

    public static QualityGateState fromString(def value){
        return this.values().findResult(NO_RESULT, { it.stringValue.equals(value) ?: null})
    }

    public String getStringValue(){
        return stringValue
    }
}