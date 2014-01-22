package org.cloudfoundry.gradle.tasks

import org.cloudfoundry.client.lib.StartingInfo
import org.cloudfoundry.client.lib.domain.CloudApplication
import org.cloudfoundry.client.lib.domain.InstanceInfo
import org.cloudfoundry.client.lib.domain.InstanceState
import org.cloudfoundry.client.lib.domain.InstancesInfo
import org.gradle.api.GradleException
import org.springframework.http.HttpStatus

class StartCloudFoundryHelper {
    static final int MAX_STATUS_CHECKS = 60

    void startApplication() {
        log "Starting ${application}"
        StartingInfo startingInfo = client.startApplication(application)
        showAppStartup(startingInfo)
    }

    void showAppStartup(StartingInfo startingInfo) {
        CloudApplication app = client.getApplication(application)

        showStagingStatus(startingInfo)
        showStartingStatus(app)
        showStartResults(app)
    }

    void showStagingStatus(StartingInfo startingInfo) {
        if (startingInfo) {
            errorHandler.addExpectedStatus(HttpStatus.NOT_FOUND)

            int offset = 0
            String staging = client.getStagingLogs(startingInfo, offset)
            while (staging != null) {
                log staging
                offset += staging.size()
                staging = client.getStagingLogs(startingInfo, offset)
            }

            errorHandler.clearExpectedStatus()
        }
    }

    void showStartingStatus(CloudApplication app) {
        log "Checking status of ${app.name}"

        errorHandler.addExpectedStatus(HttpStatus.BAD_REQUEST)

        def statusChecks = 0

        while (true) {
            List<InstanceInfo> instances = getApplicationInstances(app)

            if (instances) {
                def expectedInstances = getExpectedInstances(instances)
                def runningInstances = getRunningInstances(instances)
                def flappingInstances = getFlappingInstances(instances)

                showInstancesStatus(instances, runningInstances, expectedInstances)

                if (flappingInstances > 0)
                    break

                if (runningInstances == expectedInstances)
                    break
            }

            if (statusChecks > MAX_STATUS_CHECKS)
                break

            statusChecks++
            sleep 1000
        }

        errorHandler.clearExpectedStatus()
    }

    void showInstancesStatus(List<InstanceInfo> instances, runningInstances, expectedInstances) {
        def stateCounts = [:].withDefault { 0 }
        instances.each { instance ->
            stateCounts[instance.state] += 1
        }

        def stateStrings = []
        stateCounts.each { state, count ->
            stateStrings << "${count} ${state.toString().toLowerCase()}"
        }

        def expectedString = "${expectedInstances}"
        def runningString = "${runningInstances}".padLeft(expectedString.length())
        log "  ${runningString} of ${expectedString} instances running (${stateStrings.join(", ")})"
    }

    void showStartResults(CloudApplication app) {
        List<InstanceInfo> instances = getApplicationInstances(app)

        def expectedInstances = getExpectedInstances(instances)
        def runningInstances = getRunningInstances(instances)
        def flappingInstances = getFlappingInstances(instances)

        if (flappingInstances > 0 || runningInstances == 0) {
            throw new GradleException("Application ${application} start unsuccessful")
        } else if (runningInstances > 0) {
            List<String> uris = allUris
            if (uris.empty) {
                log "Application ${application} is available"
            } else {
                log "Application ${application} is available at ${uris.collect{"http://$it"}.join(',')}"
            }
        }

        if (expectedInstances != runningInstances) {
            log "TIP: The system will continue to start all requested app instances. Use the 'cf-app' task to monitor app status."
        }
    }

    List<InstanceInfo> getApplicationInstances(CloudApplication app) {
        InstancesInfo instancesInfo = client.getApplicationInstances(app)
        instancesInfo?.instances
    }

    def getExpectedInstances(List<InstanceInfo> instances) {
        instances.size()
    }

    def getRunningInstances(List<InstanceInfo> instances) {
        instances.count { instance -> instance.state == InstanceState.RUNNING }
    }

    def getFlappingInstances(List<InstanceInfo> instances) {
        instances.count { instance -> instance.state == InstanceState.FLAPPING }
    }
}
