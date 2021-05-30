package com.ToDo.ui.utils;

import java.util.Iterator;
import java.util.List;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;

import elemental.json.Json;
import elemental.json.JsonArray;

@Tag("multi-select-combo-box")
@HtmlImport("bower_components/multi-select-combo-box/multi-select-combo-box.html")
public class MultiselectComboBox extends AbstractSinglePropertyField<MultiselectComboBox, String> {

    public MultiselectComboBox(String label) {
        super("value", "", true);
        setLabel(label);
    }

    public void setLabel(String label) {
        getElement().setProperty("label", label);
    }

    public String getLabel() {
        return getElement().getProperty("label");
    }

    public void setItems(List<String> values) {

        Iterator<String> it = values.iterator();
        JsonArray items = Json.createArray();
        int n = 0;

        while (it.hasNext()) {
            items.set(n, it.next());
            n++;
        }

        getElement().setPropertyJson("items", items);
    }
}