package at.bfarka.sonar.qualitygate


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static at.bfarka.sonar.qualitygate.QualityGateState.ERROR
import static at.bfarka.sonar.qualitygate.QualityGateState.WARNING
import static org.hamcrest.Matchers.equalTo

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertThat

import static org.junit.Assert.assertTrue

/**
 * Created by berndfarka on 15.11.14.
 */
public class SonarQualityGatePluginTest {

    @Test
    public void testPluginRegistration(){
        InputStream propertiesFileStream = ClassLoader.systemClassLoader.getResourceAsStream("META-INF/gradle-plugins/at.bfarka.sonar.qualitygate.properties")
        assertNotNull(propertiesFileStream);

        Properties prop = new Properties()
        prop.load(propertiesFileStream);
        String implClass = prop.getProperty("implementation-class");

        Class<Plugin> pluginClass = Class.forName(implClass);
        Plugin<Project> sonarQualityGatePlugin = pluginClass.newInstance();
        assertNotNull(sonarQualityGatePlugin);
        assertTrue(sonarQualityGatePlugin instanceof SonarQualityGatePlugin);

    }

    @Test
    public void testPluginInitialisation(){
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'at.bfarka.sonar.qualitygate'
        def extension = project.extensions.findByName("sonarQualityGate")
        assertNotNull(extension)
        assertTrue(extension instanceof SonarQualityGateExtension)

        def task = project.tasks.findByName("sonarQualityGate")
        assertTrue(task instanceof SonarQualityGateTask);

    }

    @Test
    public void testDependsWithLateApply(){
        Project project = ProjectBuilder.builder().build();


        project.apply plugin: "at.bfarka.sonar.qualitygate"
        // deploy sonar-runner late (after qualitygate)
        project.apply plugin: "sonar-runner"

        assertTrue(project.tasks.findByName("sonarQualityGate").getDependsOn().contains(project.tasks.findByName("sonarRunner")))
    }


    @Test
    public void testDependswithEarlyApply(){
        Project project = ProjectBuilder.builder().build();

        // deploy sonar-runner early (before qualitygate)
        project.apply plugin: "sonar-runner"
        project.apply plugin: "at.bfarka.sonar.qualitygate"

        assertTrue(project.tasks.findByName("sonarQualityGate").getDependsOn().contains(project.tasks.findByName("sonarRunner")))

    }


    @Test
    public void testPluginWithSonarExtension(){
        Project project = ProjectBuilder.builder().build();

        project.apply plugin: "at.bfarka.sonar.qualitygate"
        project.apply plugin: "sonar-runner"

        project.sonarRunner{
                sonarProperties{
                    property "sonar.host.url","hostUrl"
                    property "sonar.projectKey","projectKey"
                    property "sonar.branch","branch"
                }
            }


        SonarQualityGateTask task = project.tasks.findByName("sonarQualityGate")
        task.execute();
        assertThat(task.sonarHostUrl, equalTo("hostUrl"))
        assertThat(task.sonarProjectKey, equalTo("projectKey"))
        assertThat(task.sonarBranch, equalTo("branch"))
        assertThat(task.qualityGateState, equalTo(WARNING))
        assertThat(task.failBuild, equalTo(true))

    }

    @Test
    public void testExtension() {
        Project project = ProjectBuilder.builder().build();

        project.apply plugin: "at.bfarka.sonar.qualitygate"
        project.apply plugin: "sonar-runner"

        project.sonarRunner{
            sonarProperties{
                property "sonar.host.url","hostUrl"
                property "sonar.projectKey", "projectKey"
                property "sonar.branch", "sonarBranch"
            }
        }

        project.sonarQualityGate {
            sonarHostUrl 'testUrl'
            sonarProjectKey 'testKey'
            sonarBranch 'testBranch'
            qualityGateState ERROR
            failBuild false
        }


        SonarQualityGateTask task = project.tasks.findByName("sonarQualityGate")
        task.execute()
        assertThat(task.sonarHostUrl, equalTo('testUrl'))
        assertThat(task.sonarBranch, equalTo('testBranch'))
        assertThat(task.sonarProjectKey, equalTo('testKey'))
        assertThat(task.qualityGateState, equalTo(ERROR))
        assertThat(task.failBuild, equalTo(false))
    }
}