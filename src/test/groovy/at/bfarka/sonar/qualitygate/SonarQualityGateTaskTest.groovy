package at.bfarka.sonar.qualitygate

import at.bfarka.sonar.qualitygate.util.MockRequest
import at.bfarka.sonar.qualitygate.util.SonarMockRule
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.Test

import static com.google.common.collect.ImmutableMap.builder
import static org.hamcrest.Matchers.equalTo
import static org.junit.Assert.assertThat

/**
 * Created by berndfarka on 23.11.14.
 */
class SonarQualityGateTaskTest {

    private Project createProject() {
        Project project = ProjectBuilder.builder().build();

        project.apply plugin: "at.bfarka.sonar.qualitygate"
        project.apply plugin: "sonar-runner"

        project.sonarRunner {
            sonarProperties {
                property "sonar.host.url", "http://127.0.0.1:8080/sonar"
                property "sonar.projectKey", "sonar-qualitygate"
                property "sonar.branch", "branch"
            }
        }
        return project
    }

    @Rule
    public SonarMockRule rule = new SonarMockRule();

    @Test
    public void testStates() {
        test("gateok.json", QualityGateState.OK)
        test("gatewarn.json", QualityGateState.WARNING)
        test("gateerror.json", QualityGateState.ERROR)

    }

    public void test(String fileName, QualityGateState expectedState) {

        rule.addMockRequest(new MockRequest("/api/resources", builder().put("metrics", "alert_status").put("resource", "sonar-qualitygate").put("format", "json").build(), fileName))

        def project = createProject()

        SonarQualityGateTask task = project.tasks.findByName("sonarQualityGate")
        task.execute();

        assertThat(task.fetchQualityGateState(), equalTo(expectedState))

    }

}
