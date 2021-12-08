package com.mini.ecommerceapp.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class SearchDTO extends BaseTime {
    @NotBlank
    private String search;

    public SearchDTO() {
    }

    public SearchDTO(@FutureOrPresent LocalDateTime startTimeStamp, @Future LocalDateTime endTimeStamp, String search) {
        super(startTimeStamp, endTimeStamp);
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
