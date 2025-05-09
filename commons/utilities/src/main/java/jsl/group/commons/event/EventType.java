package jsl.group.commons.event;

public enum EventType {
    PURCHASE("purchase"), CANCEL("cancel"), SHIPPING("shipping");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public String getType() { return this.type; }
}
