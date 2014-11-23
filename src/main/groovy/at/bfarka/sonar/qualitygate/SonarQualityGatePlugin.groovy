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
        project.extensions.add("sonarQualityGate",  SonarQualityGateExtension.class)


        QualityGateTask task = project.task('sonarQualityGate', type: QualityGateTask)

        task.conventionMapping.sonarHostUrl = conventionMapping("sonarHostUrl", project)

        project.tasks.whenTaskAdded({ addedTask ->
            if (addedTask.name == "sonarRunner") {
                task.dependsOn addedTask
            }
        })

        Task sonarRunner = project.tasks.findByName("sonarRunner")
        if (sonarRunner != null) {
            task.dependsOn sonarRunner
        }

    }


    def conventionMapping(def propName, Project project) {
        return {
            def sonarRunner = project.tasks.findByName("sonarRunner");
            def sonarQualityGate = project.extensions.findByName("sonarQualityGate");

            def returnValue = sonarQualityGate.properties.get(propName)
            if (returnValue == null) {

                if ((sonarRunner != null) && sonarRunner.sonarProperties != null) {
                    returnValue = sonarRunner.sonarProperties[propName]

                }

            }
            return returnValue;
        }


    }
}