package at.bfarka.sonar.qualitygate

import com.google.common.base.Preconditions
import groovy.json.JsonSlurper
import org.gradle.api.GradleException
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
    QualityGateState qualityGateState;
    @Input
    boolean failBuild



    public QualityGateState fetchQualityGateState() {

        String target = "${getSonarHostUrl()}/api/resources?metrics=alert_status&resource=${getSonarProjectKey()}&format=json"

        JsonSlurper slurper = new JsonSlurper()

        def results = slurper.parse(target.toURL().openStream())

        def status = null

        status = results.findResult { result ->
            result?.msr?.find { msr -> 'alert_status'.equals(msr.key) }?.data
        }
        return QualityGateState.fromString(status)
    }

    @TaskAction
    public void querySonar() {

        if(isFailBuild()){
            def fetchedState = fetchQualityGateState()
            if(fetchedState.intValue()>= getQualityGateState().intValue()){
                throw new GradleException("QualityGate check not passed! QualityGateState required: ${getQualityGateState()} but got: ${fetchedState}")
            }
        }

    }


}
