/*
 * Copyright 2014 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.orca.kato.pipeline.gce

import groovy.transform.CompileStatic
import com.netflix.spinnaker.orca.kato.tasks.MonitorKatoTask
import com.netflix.spinnaker.orca.kato.tasks.NotifyEchoTask
import com.netflix.spinnaker.orca.kato.tasks.ServerGroupCacheForceRefreshTask
import com.netflix.spinnaker.orca.kato.tasks.WaitForUpInstancesTask
import com.netflix.spinnaker.orca.kato.tasks.gce.CreateGoogleServerGroupTask
import com.netflix.spinnaker.orca.pipeline.LinearStage
import org.springframework.batch.core.Step
import org.springframework.stereotype.Component

@Component
@CompileStatic
class DeployGoogleServerGroupStage extends LinearStage {

  public static final String MAYO_CONFIG_TYPE = "deploy_gce"

  DeployGoogleServerGroupStage() {
    super(MAYO_CONFIG_TYPE)
  }

  @Override
  protected List<Step> buildSteps() {
    def step1 = buildStep("createDeploy", CreateGoogleServerGroupTask)
    def step2 = buildStep("monitorDeploy", MonitorKatoTask)
    def step3 = buildStep("forceCacheRefresh", ServerGroupCacheForceRefreshTask)
    def step4 = buildStep("waitForUpInstances", WaitForUpInstancesTask)
    def step5 = buildStep("forceCacheRefresh", ServerGroupCacheForceRefreshTask)
    def step6 = buildStep("sendNotification", NotifyEchoTask)
    [step1, step2, step3, step4, step5, step6]
  }
}
