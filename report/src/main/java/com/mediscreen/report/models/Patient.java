package com.mediscreen.report.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Patient {

    private Long      id;
    private String    family;
    private String    given;
    private LocalDate dob;
    private Character sex;
    private String    address;
    private String    phone;

    public Patient() {

    }

    public Long getId() {

        return this.id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getFamily() {

        return this.family;
    }

    public void setFamily(String family) {

        this.family = family;
    }

    public String getGiven() {

        return this.given;
    }

    public void setGiven(String given) {

        this.given = given;
    }

    public LocalDate getDob() {

        return this.dob;
    }

    public void setDob(CharSequence dob) {

        this.dob = LocalDate.parse(dob, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public char getSex() {

        return this.sex;
    }

    public void setSex(char sex) {

        this.sex = sex;
    }

    public String getAddress() {

        return this.address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public String getPhone() {

        return this.phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }
}
