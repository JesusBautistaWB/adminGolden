package com.intabux.gobussines.Models;

public class Order {

    String id;
    String idDeliveryMan;
    String idBusiness;
    String key;
    String user;
    String commentary;
    int status;
    Double totalProducts;
    Double ShippingCost;
    long dateCreateOrder;
    long dateFinishOrder;
    int typePayment; //0-cash 1-card
    Double businessLat;
    Double businessLng;
    Double destinationLat;
    Double destinationLng;
    Double originLat;
    Double originLng;
    String origin;
    String destination;

    public Double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(Double originLat) {
        this.originLat = originLat;
    }

    public Double getOriginLng() {
        return originLng;
    }

    public void setOriginLng(Double originLng) {
        this.originLng = originLng;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Order(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getShippingCost() {
        return ShippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        ShippingCost = shippingCost;
    }

    public String getIdDeliveryMan() {
        return idDeliveryMan;
    }

    public void setIdDeliveryMan(String idDeliveryMan) {
        this.idDeliveryMan = idDeliveryMan;
    }

    public String getIdBusiness() {
        return idBusiness;
    }

    public void setIdBusiness(String idBusiness) {
        this.idBusiness = idBusiness;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Double getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Double totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getDateCreateOrder() {
        return dateCreateOrder;
    }

    public void setDateCreateOrder(long dateCreateOrder) {
        this.dateCreateOrder = dateCreateOrder;
    }

    public long getDateFinishOrder() {
        return dateFinishOrder;
    }

    public void setDateFinishOrder(long dateFinishOrder) {
        this.dateFinishOrder = dateFinishOrder;
    }

    public int getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(int typePayment) {
        this.typePayment = typePayment;
    }

    public Double getBusinessLat() {
        return businessLat;
    }

    public void setBusinessLat(Double businessLat) {
        this.businessLat = businessLat;
    }

    public Double getBusinessLng() {
        return businessLng;
    }

    public void setBusinessLng(Double businessLng) {
        this.businessLng = businessLng;
    }

    public Double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(Double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public Double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(Double destinationLng) {
        this.destinationLng = destinationLng;
    }
}
