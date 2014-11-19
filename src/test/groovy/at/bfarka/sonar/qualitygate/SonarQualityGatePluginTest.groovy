package at.bfarka.sonar.qualitygate


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test
import java.util.Properties

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNotNull
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

}