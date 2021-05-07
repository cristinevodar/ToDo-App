package com.ToDo.backend.data.entity;

import com.vaadin.flow.shared.util.SharedUtil;

import java.util.Locale;

    public enum Status {
        FINISHED, INPROGRESS, NEW;

        public String getDisplayName() {
            return SharedUtil.capitalize(name().toLowerCase(Locale.ENGLISH));
        }


    }

