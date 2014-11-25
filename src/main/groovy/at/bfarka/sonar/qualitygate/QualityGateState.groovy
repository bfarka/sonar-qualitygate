package at.bfarka.sonar.qualitygate

/**
 * Created by berndfarka on 25.11.14.
 */
enum QualityGateState {
    OK('OK'), WARNING('WARN'),ERROR('ERROR')


    private final stringValue

    private QualityGateState(def stringValue){
        this.stringValue = stringValue
    }

    public static QualityGateState fromString(def value){
        this.values().each{
            if(it.stringValue.equals(value)){
                return it
            }
        }
        return null

    }

}