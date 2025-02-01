package miau.dona;

import java.util.Date;

public class Package {
    private static Long idPackage = 0L;
    private final double weight;
    private final double height;
    private final double width;
    private User sender;
    private User receiver;
    private String address;
    private Date shippingDate;
    private Date pickupDate;

    public Package(double weight, double height, double width) {
        idPackage++;
        this.weight = weight;
        this.height = height;
        this.width = width;
    }


    
    public void sendPackage(User sender, User receiver, String address, Date shippingDate) {
        this.sender = sender;
        this.receiver = receiver;
        this.address = address;
        this.shippingDate = shippingDate;

        System.out.println("Packet sent");
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Long getIdPackage() {
        return idPackage;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}

