package at.bfarka.sonar.qualitygate

import at.bfarka.sonar.qualitygate.util.MockRequest
import at.bfarka.sonar.qualitygate.util.SonarMockRule
import com.google.common.collect.ImmutableMap
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.Test

/**
 * Created by berndfarka on 23.11.14.
 */
class SonarQualityGateTaskTest {

    @Rule
    public SonarMockRule rule = new SonarMockRule();

    @Test
    public void test() {
        ClassLoader.getSystemResourceAsStream("gateok.json")
        rule.addMockRequest(new MockRequest("/api/resources", ImmutableMap.builder().put("metrics", "alert_status").put("resource", "sonar-qualitygate").put("format", "json").build(), "gateok.json"))

        Project project = ProjectBuilder.builder().build();

        project.apply plugin: "at.bfarka.sonar.qualitygate"
        project.apply plugin: "sonar-runner"

        project.sonarRunner{
            sonarProperties{
                property "sonar.host.url","http://127.0.0.1:8080/sonar"
                property "sonar.projectKey","sonar-qualitygate"
                property "sonar.branch","branch"
            }
        }


        SonarQualityGateTask task = project.tasks.findByName("sonarQualityGate")
        task.execute();

        task.fetchQualityGateState();

    }

}
