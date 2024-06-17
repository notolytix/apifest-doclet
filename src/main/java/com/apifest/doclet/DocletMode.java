/*
* Copyright 2013-2015, ApiFest project
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

package com.apifest.doclet;

/**
 * Contains all the doclet modes.
 * @author Ivan Georgiev
 *
 */
public enum DocletMode {
    MAPPING("mapping"), DOC("doc"), OPEN_API("openAPI");

    private String mode;

    private DocletMode(String mode) {
        this.mode = mode;
    }

    public String getValue() {
        return this.mode;
    }

    public static DocletMode fromString(String value) {
        DocletMode[] modes = DocletMode.values();
        for (DocletMode mode : modes) {
            if (mode.getValue().equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return null;
    }
}
