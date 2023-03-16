package com.redhood.issuetracker.service.account.permissions.handler;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PermissionsEnum {
    READ(4),
    ADD(3),
    DELETE(2),
    UPDATE(1);

    private int position;

    PermissionsEnum(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public static PermissionsEnum get(int position) {
        return Stream.of(PermissionsEnum.values())
                .collect(Collectors.toMap(PermissionsEnum::getPosition, Function.identity()))
                .get(position);
    }
}
