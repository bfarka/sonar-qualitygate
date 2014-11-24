package at.bfarka.sonar.qualitygate

import groovy.json.JsonSlurper
import org.apache.http.client.fluent.Request
import org.gradle.api.DefaultTask
import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Created by berndfarka on 19.11.14.
 */
class SonarQualityGateTask extends ConventionTask{

    @Input
    String sonarHostUrl;
    @Input
    String sonarProjectKey;
    @Input
    String sonarBranch


    public String fetchQualityGateState(){

        String target =  "${getSonarHostUrl()}/api/resources?metrics=alert_status&resource=$getSonarProjectKey()}&format=json"

        def slurper = new JsonSlurper()

        def result = slurper.parse(target.toURL())


        return result.msr.data;
    }

@TaskAction
    public void querySonar(){
        println getSonarHostUrl()
    }


}
