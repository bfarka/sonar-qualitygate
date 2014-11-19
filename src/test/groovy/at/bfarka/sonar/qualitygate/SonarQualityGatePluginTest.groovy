package at.bfarka.sonar.qualitygate


import org.gradle.api.Plugin
import org.gradle.api.Project
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
        Plugin<Project> sshScpPlugin = pluginClass.newInstance();
        assertNotNull(sshScpPlugin);
        assertTrue(sshScpPlugin instanceof SonarQualityGatePlugin);

    }

}