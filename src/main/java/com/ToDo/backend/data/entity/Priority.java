package com.ToDo.backend.data.entity;

import com.vaadin.flow.shared.util.SharedUtil;

import java.util.Locale;

public enum Priority {

    LOW, MEDIUM, HIGH;

    public String getDisplayName() {
        return SharedUtil.capitalize(name().toLowerCase(Locale.ENGLISH));
    }

}
