package com.vein.vein.shared.data.enums;

import java.util.Arrays;
import java.util.List;

public enum Status {
    ACTIVE(1, "Online"),
    INACTIVE(2, "Offline");

    private final Integer code;
    private final String description;

    Status(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static List<StatusInfo> listInfo(){
        return Arrays.stream(
                Status.values()).map(
                        status -> new StatusInfo(status.getCode(), status.getDescription())
        ).toList();
    }

    public static Status fromCode(int code){
        return Arrays.stream(
                Status.values())
                .filter(status -> status.getCode() == code)
                .findFirst()
                .orElse(null);
    }

}
