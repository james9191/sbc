/*******************************************************************************
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc, Eolos IT Corp and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */
package org.restcomm.sbc.managers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


import org.apache.log4j.Logger;
import org.restcomm.sbc.bo.NetworkPoint;
import org.restcomm.sbc.bo.NetworkPoint.Tag;
import org.restcomm.sbc.bo.shiro.ShiroResources;
import org.restcomm.sbc.dao.DaoManager;
import org.restcomm.sbc.dao.NetworkPointsDao;
import org.restcomm.sbc.managers.controller.ManagementProvider;
import org.restcomm.sbc.managers.controller.ManagementProviderFactory;



/**
 * @author  ocarriles@eolos.la (Oscar Andres Carriles)
 * @date    27 jul. 2016 16:36:38
 * @class   NetworkManager.java
 *
 */
public class NetworkManager  {
	
	private static transient Logger LOG = Logger.getLogger(NetworkManager.class);
	

	private static ArrayList<NetworkPoint> eths;
	private static ArrayList<NetworkPoint> tots;


	static {
		eths = new ArrayList<NetworkPoint>();
		tots = new ArrayList<NetworkPoint>();
		try {
			init();
		} catch (IOException e) {
			LOG.error("Can't Obtain Interface data", e);
		}
		
	}
	
	public static List<NetworkPoint> getNetworkPoints() {
		return eths;
	}
	
	public static List<NetworkPoint> getPersistentNetworkPoints() {
		mergeNetworkPoints();
		return tots;
	}
	
	private static void mergeNetworkPoints() {
		DaoManager daos=ShiroResources.getInstance().get(DaoManager.class);	
		NetworkPointsDao dao = daos.getNetworkPointDao();
		List<NetworkPoint> persistents=dao.getNetworkPoints();
		tots = new ArrayList<NetworkPoint>(eths);
		for(NetworkPoint realPoint:eths) {
			for(NetworkPoint persistentPoint:persistents) {
				if(persistentPoint.getId().equals(realPoint.getId())) {
					persistentPoint.setMacAddress(realPoint.getMacAddress());
					persistentPoint.setDescription(realPoint.getDescription());
					persistentPoint.setAddress(realPoint.getAddress());
					persistentPoint.setAccountSid(realPoint.getAccountSid());
					tots.remove(realPoint);
					tots.add(persistentPoint);		
				}
			}
			
		}
	
	}
	
	public static NetworkPoint getNetworkPoint(String id) {
		for(NetworkPoint point:eths) {
			if(point.getId().equals(id)) {
				return point;
			}
		}
		return null;
	}
	
	public static String getIpAddress(String npoint) {
		NetworkPoint point=getNetworkPoint(npoint);
		if(point!=null&& point.getAddress()!=null)
			return point.getAddress().getHostAddress();
		
		return null;
	}
	
	public static NetworkPoint getNetworkPointByIpAddress(String ipAddress) {
		for(NetworkPoint point:eths) {
			if(LOG.isTraceEnabled()) {
				//LOG.trace("Searching points "+point.toPrint());
			}
			if(point.getAddress().getHostAddress().equals(ipAddress)) {
				if(LOG.isTraceEnabled()) {
					LOG.trace("Found point "+point.toPrint());
				}
				return point;
			}
		}
		return null;
	}
	public static NetworkPoint getPersistentNetworkPointByIpAddress(String ipAddress) {
		for(NetworkPoint point:tots) {
			if(LOG.isTraceEnabled()) {
				//LOG.trace("Searching points "+point.toPrint());
			}
			if(point.getAddress().getHostAddress().equals(ipAddress)) {
				if(LOG.isTraceEnabled()) {
					LOG.trace("Found point "+point.toPrint());
				}
				return point;
			}
		}
		return null;
	}
	
	public static Tag getTag(String ipAddress) {
		NetworkPoint point=getPersistentNetworkPointByIpAddress(ipAddress);
		return point.getTag();
	}
	
	public static boolean exists(String id) {
		for(NetworkPoint point:eths) {
			if(point.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	private static void init() throws IOException {
			ManagementProvider jmxManager = null;
			int id=0;
			try {
				jmxManager = ManagementProviderFactory.getProvider();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				LOG.error("JMX Error", e);
			}
			
	        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
	        int group=0;
	        for (NetworkInterface netIf : Collections.list(nets)) {
	        	String mac=makeMAC(netIf.getHardwareAddress());
	        	if("".equals(mac.trim())) {
	        		continue;
	        	}
	        	List<InterfaceAddress> inetAddresses =  netIf.getInterfaceAddresses();
	 	       
		        for (InterfaceAddress inetAddress : inetAddresses) {
		        	NetworkPoint point=new NetworkPoint("eth-"+mac+"-"+id, inetAddress.getAddress());
		        	jmxManager.addInterface("eth-"+mac+"-"+id, inetAddress.getAddress().getHostAddress());
		        	point.setGroup(group);
		        	point.setDescription(netIf.getDisplayName());
		        	point.setMacAddress(mac);
		        	point.setAddress(inetAddress.getAddress());
		        	point.setBroadcast(inetAddress.getBroadcast());
		        	point.setPrefixMask(inetAddress.getNetworkPrefixLength());
		        	
		        	id++;
		            eths.add(point);
		            if(LOG.isInfoEnabled()) {
		            	LOG.info("Detected interface "+point.toPrint());
		            }
		        }
		        group++;
		        id=0;
	         
	        }
	        mergeNetworkPoints();
	       
	    }
	
	    private static String makeMAC(byte[] mac) {
	    	StringWriter writer = new StringWriter();
			PrintWriter out = new PrintWriter(writer);
	    	if(mac==null) {
	    		return "";
	    	}
	    		
			for(int i=0;i<mac.length;i++){
				out.format("%02X", mac[i]);
			}
			return writer.toString();
	    }
	    
	  

}