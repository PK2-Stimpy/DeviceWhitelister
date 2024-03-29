/*
 * Copyright 2014 samuelcampos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.samuelcampos.usbdrivedetector.process;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author samuelcampos
 */
@Slf4j
public class CommandExecutor {
    public OutputProcessor executeCommand(final String command) throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("Running command: {}", command);
        }

        Process process = Runtime.getRuntime().exec(command);

        return new CommandOutputProcessor(command, process);
    }

}
