package org.restcomm.chain.processor.impl;


import javax.servlet.sip.SipServletMessage;

import org.restcomm.chain.processor.impl.MutableMessage;
import org.restcomm.chain.processor.Message;

public class SIPMutableMessage implements  MutableMessage {
	
	private SipServletMessage  content;
	private String sourceLocalAddress;
	private String targetLocalAddress;
	private String sourceRemoteAddress;
	private String targetRemoteAddress;
	private String sourceTransport;
	private String targetTransport;
	
	private int direction;
	private int target;
	private Object metadata;
	
	
	private boolean linked=true;
	private boolean aborted=false;
	
	public SIPMutableMessage(SipServletMessage content) {	
		this.content=content;
	}
	
	@Override
	public SipServletMessage getContent() {
		return content;
	}

	@Override
	public void setContent(Object content) {
		this.content=(SipServletMessage) content;
		
	}

	@Override
	public void unlink() {
		linked=false;
		
	}

	@Override
	public boolean isLinked() {
		return linked;
	}
	
	@Override
	public boolean isAborted() {
		return aborted;
	}

	@Override
	public void abort() {
		aborted=true;
		
	}

	@Override
	public int getDirection() {
		return direction;
	}


	public void setDirection(int direction) {
		this.direction = direction;
	}

	@Override
	public String getSourceLocalAddress() {
		return sourceLocalAddress;
	}

	public void setSourceLocalAddress(String sourceLocalAddress) {
		this.sourceLocalAddress = sourceLocalAddress;
	}

	@Override
	public String getTargetLocalAddress() {
		return targetLocalAddress;
	}

	public void setTargetLocalAddress(String targetLocalAddress) {
		this.targetLocalAddress = targetLocalAddress;
	}

	@Override
	public String getSourceRemoteAddress() {
		return sourceRemoteAddress;
	}

	public void setSourceRemoteAddress(String sourceRemoteAddress) {
		this.sourceRemoteAddress = sourceRemoteAddress;
	}

	@Override
	public String getTargetRemoteAddress() {
		return targetRemoteAddress;
	}

	public void setTargetRemoteAddress(String targetRemoteAddress) {
		this.targetRemoteAddress = targetRemoteAddress;
	}

	@Override
	public Object getMetadata() {
		return metadata;
	}
	
	
	public void setMetadata(Object metadata) {
		this.metadata=metadata;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}
	
	public String toString() {
		return
				this.getClass().getSimpleName()+"\n{"+
				" "+(direction==Message.SOURCE_DMZ?" source=DMZ":" source=MZ")+
				" target="+Message.TARGET[target]+"\n"+
				" source transport="+sourceTransport+
				" target transport="+targetTransport+"\n"+
				" source local address="+sourceLocalAddress+
				" target local address="+targetLocalAddress+"\n"+
				" source remote address="+sourceRemoteAddress+
				" target remote address="+targetRemoteAddress+"\n}";							
	}

	public String getSourceTransport() {
		return sourceTransport;
	}

	public void setSourceTransport(String sourceTransport) {
		this.sourceTransport = sourceTransport;
	}

	public String getTargetTransport() {
		return targetTransport;
	}

	public void setTargetTransport(String targetTransport) {
		this.targetTransport = targetTransport;
	}

}
