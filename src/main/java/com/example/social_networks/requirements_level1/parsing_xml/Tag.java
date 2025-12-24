package com.example.social_networks.requirements_level1.parsing_xml;

public class Tag {

    public String name;
    public boolean isOpening;
    public boolean isClosing;
    public String innerText;

    Tag() {
        this.isOpening = true;
        this.isClosing = false;
        innerText = "";
    }
}
