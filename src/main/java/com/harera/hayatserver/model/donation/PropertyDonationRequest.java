package com.harera.hayatserver.model.donation;

import lombok.Data;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.harera.hayatserver.model.BaseEntity;

@Data
public class PropertyDonationRequest extends BaseEntity {

    private String title;
    private String description;
    private Integer rooms;
    private Integer bathrooms;
    private Integer floors;
    private Integer kitchens;

    @JsonProperty("available_from")
    private ZonedDateTime AvailableFrom;
    @JsonProperty("available_to")
    private ZonedDateTime AvailableTo;
    @JsonProperty("city_id")
    private long cityId;
    @JsonProperty("communication_type_id")
    private long communicationTypeId;
}