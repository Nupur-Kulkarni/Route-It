package com.example.deepika.travelguide.autocomplete;

/**
 * Created by deepi on 5/5/2018.
 */

public class AutoCompleteBean {
    private String description;
    private String reference;

    public AutoCompleteBean(String description, String reference){
        this.description=description;
        this.reference=reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}

