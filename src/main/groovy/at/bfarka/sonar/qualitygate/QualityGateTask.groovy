package at.bfarka.sonar.qualitygate

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input

/**
 * Created by berndfarka on 19.11.14.
 */
class QualityGateTask extends DefaultTask{

    @Input
    String sonarHostUrl;
    @Input
    String sonarProjectKey;
    @Input
    String sonarBranch

}
