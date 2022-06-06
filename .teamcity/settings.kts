import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.kubernetesCloudImage
import jetbrains.buildServer.configs.kotlin.kubernetesCloudProfile
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2022.04"

project {

    buildType(Build)

    features {
        kubernetesCloudImage {
            id = "PROJECT_EXT_4"
            profileId = "kube-4"
            agentPoolId = "-2"
            agentNamePrefix = "todo-agent"
            maxInstancesCount = 2
            podSpecification = deploymentTemplate {
                deploymentName = "teamcity-agents"
            }
        }
        kubernetesCloudProfile {
            id = "kube-4"
            name = "buildagent"
            description = "buildagent"
            terminateIdleMinutes = 30
            apiServerURL = "https://192.168.64.2:16443"
            namespace = "teamcity"
            authStrategy = token {
                token = "credentialsJSON:c7c3d262-b47e-4405-8e8a-648595564156"
            }
            param("kubeconfigContext", "")
        }
    }
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    triggers {
        vcs {
            perCheckinTriggering = true
            enableQueueOptimization = false
        }
    }
})
