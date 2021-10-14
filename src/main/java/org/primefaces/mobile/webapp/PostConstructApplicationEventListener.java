/*
 * Copyright 2009-2013 PrimeTek.
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
package org.primefaces.mobile.webapp;

import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.primefaces.util.LangUtils;


public class PostConstructApplicationEventListener implements SystemEventListener {

    private final static Logger logger = Logger.getLogger(PostConstructApplicationEventListener.class.getName());

    public boolean isListenerForSource(Object source) {
        return true;
    }

    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException 
    {
    	 String buildVersion = this.resolveBuildVersion();

    	 logger.log(Level.INFO, "Running on PrimeFaces-Mobile {0}", buildVersion);
    }
    
    
    protected String resolveBuildVersion() {
        String buildVersion = null;

        try (InputStream is = getClass().getResourceAsStream("/META-INF/maven/org.primefaces/primefaces-mobile/pom.properties")) {
            Properties buildProperties = new Properties();
            buildProperties.load(is);
            buildVersion = buildProperties.getProperty("version");
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "PrimeFaces-Mobile version not resolvable - Could not load pom.properties.");
        }

        // This should only happen if PF + the webapp is openend and started in the same netbeans instance
        // Fallback to a UID to void a empty version in the resourceUrls
        if (LangUtils.isValueBlank(buildVersion)) {
            buildVersion = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        }

        return buildVersion;
    }
}
