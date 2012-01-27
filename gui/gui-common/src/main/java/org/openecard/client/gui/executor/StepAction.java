/*
 * Copyright 2012 Tobias Wich ecsec GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openecard.client.gui.executor;

import java.util.Map;
import org.openecard.client.gui.StepResult;


/**
 *
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public abstract class StepAction {

    private final String stepName;

    public StepAction(String stepName) {
	this.stepName = stepName;
    }


    public String associatedStepName() {
	return stepName;
    }

    public abstract StepActionResult perform(Map<String,ExecutionResults> oldResults, StepResult result);

}
