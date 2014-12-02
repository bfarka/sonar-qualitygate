package at.bfarka.sonar.qualitygate

/**
 * Created by berndfarka on 19.11.14.
 */
class SonarQualityGateExtension {

    public SonarQualityGateExtension(){
        this.qualityGateState = QualityGateState.WARNING
    }

     String sonarHostUrl

     String sonarProjectKey

     String sonarBranch

     QualityGateState qualityGateState

    boolean failBuild = true

}
