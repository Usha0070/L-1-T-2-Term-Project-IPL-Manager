package com.example.demonew.server;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private String country;
    private int age;
    private double height;
    private String club;
    private String position;
    private Integer number; // Optional
    private int weeklySalary;
    private int basePrice;
    private boolean isOnSale; // New attribute

    public Player(String name, String country, int age, double height, String club, String position, Integer number, int weeklySalary, int basePrice, boolean isOnSale) {
        this.name = name;
        this.country = country;
        this.age = age;
        this.height = height;
        this.club = club;
        this.position = position;
        this.number = number;
        this.weeklySalary = weeklySalary;
        this.basePrice = basePrice;
        this.isOnSale = isOnSale;
    }


    // Getters
    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public String getClub() {
        return club;
    }

    public String getPosition() {
        return position;
    }

    public Integer getNumber() {
        return number;
    }

    public int getWeeklySalary() {
        return weeklySalary;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    // Setters
    public void setClub(String club) {
        this.club = club;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player Name: ").append(name).append(", ")
                .append("Country: ").append(country).append(", ")
                .append("Age: ").append(age).append(", ")
                .append("Height: ").append(height).append(", ")
                .append("Club: ").append(club).append(", ")
                .append("Position: ").append(position).append(", ")
                .append(number != null ? "Jersey Number: " + number + ", " : "Jersey Number: N/A, ")
                .append("Weekly Salary: ").append(weeklySalary).append(", ")
                .append("Base Price: ").append(basePrice).append(", ")
                .append("On Sale: ").append(isOnSale ? "Yes" : "No");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Player player = (Player) obj;
        // Compare fields that uniquely identify a player
        return name.equals(player.name) &&
                age == player.age &&
                Double.compare(height, player.height) == 0 &&
                club.equals(player.club) &&
                position.equals(player.position) &&
                (number == null ? player.number == null : number.equals(player.number));
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + country.hashCode();
        result = 31 * result + age;
        result = 31 * result + Double.hashCode(height);
        result = 31 * result + club.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }
}
