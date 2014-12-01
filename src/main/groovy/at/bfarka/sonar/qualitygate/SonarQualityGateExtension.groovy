package at.bfarka.sonar.qualitygate

/**
 * Created by berndfarka on 19.11.14.
 */
class SonarQualityGateExtension {

    public SonarQualityGateExtension(){
        this.failOnState = QualityGateState.WARNING
    }

    def String sonarHostUrl

    def String sonarProjectKey

    def String sonarBranch

    def QualityGateState failOnState = QualityGateState.ERROR


}
