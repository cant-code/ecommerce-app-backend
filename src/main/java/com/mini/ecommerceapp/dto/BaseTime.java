package com.mini.ecommerceapp.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

public class BaseTime {
    @FutureOrPresent
    private LocalDateTime startTimeStamp;
    @Future
    private LocalDateTime endTimeStamp;

    public BaseTime(@FutureOrPresent LocalDateTime startTimeStamp, @Future LocalDateTime endTimeStamp) {
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
    }

    public BaseTime() {
    }

    public LocalDateTime getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(LocalDateTime startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public LocalDateTime getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(LocalDateTime endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }
}
