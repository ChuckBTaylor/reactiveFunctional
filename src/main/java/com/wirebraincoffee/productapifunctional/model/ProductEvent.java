package com.wirebraincoffee.productapifunctional.model;

public class ProductEvent {

	
	private String eventType;

	private Long eventId;
	
	public ProductEvent(String eventType, Long eventId) {
		this.eventType = eventType;
		this.eventId = eventId;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	@Override
	public String toString() {
		return "ProductEvent {\"eventType\":\"" + eventType + "\", eventId\":\"" + eventId + "}";
	}
}
