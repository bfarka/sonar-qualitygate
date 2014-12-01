package at.bfarka.sonar.qualitygate

import groovy.json.JsonSlurper
import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Created by berndfarka on 19.11.14.
 */
class SonarQualityGateTask extends ConventionTask {

    @Input
    String sonarHostUrl;
    @Input
    String sonarProjectKey;
    @Input
    String sonarBranch
    @Input
    QualityGateState failOnState;


    public QualityGateState fetchQualityGateState() {

        String target = "${getSonarHostUrl()}/api/resources?metrics=alert_status&resource=${getSonarProjectKey()}&format=json"

        def slurper = new JsonSlurper()

        def results = slurper.parse(target.toURL())

        def status = null

        status = results.findResult { result ->
            result?.msr?.find { msr -> 'alert_status'.equals(msr.key) }?.data
        }
        return QualityGateState.fromString(status)
    }

    @TaskAction
    public void querySonar() {
        println getSonarHostUrl()
    }


}
