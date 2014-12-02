package at.bfarka.sonar.qualitygate

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Created by berndfarka on 19.11.14.
 */
class SonarQualityGatePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.add("sonarQualityGate",  SonarQualityGateExtension)


        SonarQualityGateTask task = project.task('sonarQualityGate', type: SonarQualityGateTask)

        task.conventionMapping.sonarHostUrl = conventionMapping("sonarHostUrl","sonar.host.url", project)
        task.conventionMapping.sonarBranch = conventionMapping("sonarBranch", "sonar.branch", project)
        task.conventionMapping.sonarProjectKey = conventionMapping("sonarProjectKey","sonar.projectKey", project)
        task.conventionMapping.qualityGateState = {project.extensions.sonarQualityGate.qualityGateState}
        task.conventionMapping.failBuild = {project.extensions.sonarQualityGate.failBuild}

        def depends = {
            if (it.name == 'sonarRunner') {
                task.dependsOn it
            }
        }

        project.tasks.each depends
        project.tasks.whenTaskAdded depends

    }


    def conventionMapping(def propName,def sonarRunnerName, Project project) {
        return {
            def sonarRunner = project.tasks.findByName("sonarRunner");
            def sonarQualityGate = project.extensions.findByName("sonarQualityGate");

            def returnValue = sonarQualityGate.properties.get(propName)
            if (returnValue == null) {

                if ((sonarRunner != null) && sonarRunner.sonarProperties != null) {
                    returnValue = sonarRunner.sonarProperties[sonarRunnerName]

                }

            }
            return returnValue;
        }


    }
}