/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.util.HashSet;
import java.util.Set;

import javax.portlet.PortletRequest;

import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Controller for handling Portlet view mode
 *
 * @author Richard Good
 */
@Controller
@RequestMapping("VIEW")
public class ViewSessionListController
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private ConferenceUserService conferenceUserService;
	private ConferenceUserDao conferenceUserDao;
	
	@Autowired
    public void setConferenceUserService(ConferenceUserService conferenceUserService) {
        this.conferenceUserService = conferenceUserService;
    }

	@Autowired
    public void setConferenceUserDao(ConferenceUserDao conferenceUserDao) {
        this.conferenceUserDao = conferenceUserDao;
    }

	@RenderMapping
	public String view(PortletRequest request, ModelMap model)
	{
		final ConferenceUser conferenceUser = this.conferenceUserService.getCurrentConferenceUser();
		//TODO need logic like this to find "alias" users, perhaps we deal with this more at the data model level by merging users together
		//this.conferenceUserDao.findAllMatchingUsers(blackboardUser.getEmail(), blackboardUser.getAttributes());
		
		final Set<Session> sessions = new HashSet<Session>();
		
		final Set<Session> ownedSessionsForUser = this.conferenceUserDao.getOwnedSessionsForUser(conferenceUser);
        sessions.addAll(ownedSessionsForUser);
            
        final Set<Session> chairedSessionsForUser = this.conferenceUserDao.getChairedSessionsForUser(conferenceUser);
        sessions.addAll(chairedSessionsForUser);
        
        final Set<Session> nonChairedSessionsForUser = this.conferenceUserDao.getNonChairedSessionsForUser(conferenceUser);
        sessions.addAll(nonChairedSessionsForUser);

		model.addAttribute("sessions", sessions);
		
		//TODO get & add recordings, presentations & multimediate files
		return "BlackboardVCPortlet_view";
	}
}