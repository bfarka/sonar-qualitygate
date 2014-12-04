package at.bfarka.sonar.qualitygate

import at.bfarka.sonar.qualitygate.util.MockRequest
import at.bfarka.sonar.qualitygate.util.SonarMockRule
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static com.google.common.collect.ImmutableMap.builder
import static org.hamcrest.Matchers.equalTo
import static org.junit.Assert.assertThat

/**
 * Created by berndfarka on 23.11.14.
 */
class SonarQualityGateTaskTest {

    @Rule
    public final SonarMockRule mockServer = new SonarMockRule();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();


    private Project createProject() {
        Project project = ProjectBuilder.builder().build();

        project.apply plugin: "at.bfarka.sonar.qualitygate"
        project.apply plugin: "sonar-runner"

        project.sonarRunner {
            sonarProperties {
                property "sonar.host.url", mockServer.baseUrl
                property "sonar.projectKey", "sonar-qualitygate"
                property "sonar.branch", "branch"
            }
        }
        return project
    }



    @Test
    public void testStates() {
        test("gateok.json", QualityGateState.OK)
        test("gatewarn.json", QualityGateState.WARNING)
        test("gateerror.json", QualityGateState.ERROR)
        test("gatemissingresult.json", QualityGateState.NO_RESULT)

    }


    public void test(String fileName, QualityGateState expectedState) {

        mockServer.addMockRequest('sonar-qualitygate', fileName)
        def project = createProject()
        project.
            sonarQualityGate{
                failBuild false
            }

        SonarQualityGateTask task = project.tasks.findByName("sonarQualityGate")
        task.execute();

        assertThat(task.fetchQualityGateState(), equalTo(expectedState))
        mockServer.resetServer();

    }

    @Test
    public void testFailBuild(){
        mockServer.addMockRequest('sonar-qualitygate', "gateerror.json")

        def project = createProject()
        expectedException.expect(GradleException.class)
        project.tasks.sonarQualityGate.execute()

    }

}
