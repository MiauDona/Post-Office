package miau.dona;

import java.util.Date;

public class Package {
    private Long idPackage = (long) 0;
    private double weight;
    private double height;
    private double width;
    private String sender;
    private String receiver;
    private String address;
    private Date shippingDate;
    private Date pickupDate;

    private void incrementId() {
        idPackage++;
    }
    
    Package() {
        idPackage = idPackage + 1;
    }
    
    Package(double weight, double height, double width) {
        idPackage = idPackage + 1;
        this.weight = weight;
        this.height = height;
        this.width = width;
    }
    
    public void sendPackage(User sender, User receiver, String address, Date shippingDate) {
        this.sender = sender.getUsername();
        this.receiver = receiver.getUsername();
        this.address = address;
        this.shippingDate = shippingDate;

        System.out.println("Packet sent");
    }
    
    public void pickupPackage(Date pickupDate) {
        this.pickupDate = pickupDate;
        
        System.out.println("Packet picked up");
    }
}

