package at.bfarka.sonar.qualitygate


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test
import java.util.Properties

import static org.hamcrest.Matchers.contains
import static org.hamcrest.Matchers.equalTo
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertThat
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
        Assert.assertNotNull(extension)
        Assert.assertTrue(extension instanceof SonarQualityGateExtension)

        def task = project.tasks.findByName("sonarQualityGate")
        Assert.assertTrue(task instanceof QualityGateTask);

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
                    property "sonarHostUrl","hostUrl"
                }
            }


        QualityGateTask task = project.tasks.findByName("sonarQualityGate")
        task.execute();
        assertThat(task.sonarHostUrl, equalTo("hostUrl"))

    }

    @Test
    public void testExtension() {
        Project project = ProjectBuilder.builder().build();

        project.apply plugin: "at.bfarka.sonar.qualitygate"
        project.apply plugin: "sonar-runner"

        project.sonarRunner{
            sonarProperties{
                property "sonarHostUrl","hostUrl"
            }
        }

        project.sonarQualityGate {
            sonarHostUrl 'testUrl'
        }


        QualityGateTask task = project.tasks.findByName("sonarQualityGate")
        task.execute()
        Assert.assertThat(task.sonarHostUrl, equalTo('testUrl'))
    }
}