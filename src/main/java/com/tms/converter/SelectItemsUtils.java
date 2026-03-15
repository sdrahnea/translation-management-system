/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.converter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

/**
 * Utility methods for resolving submitted select values without relying on
 * JSF implementation internals.
 */
public final class SelectItemsUtils {

    private SelectItemsUtils() {
    }

    public static Object findValueByStringConversion(FacesContext context, UIComponent component, String value, Converter converter) {
        if (component == null || value == null) {
            return null;
        }
        return findValueByStringConversion(context, component, collectSelectItems(component).iterator(), value, converter);
    }

    private static Object findValueByStringConversion(FacesContext context, UIComponent component, Iterator<SelectItem> items, String value, Converter converter) {
        while (items.hasNext()) {
            SelectItem item = items.next();
            if (item == null) {
                continue;
            }
            if (item instanceof SelectItemGroup) {
                SelectItem[] subitems = ((SelectItemGroup) item).getSelectItems();
                if (!isEmpty(subitems)) {
                    Object object = findValueByStringConversion(context, component, new ArrayIterator(subitems), value, converter);
                    if (object != null) {
                        return object;
                    }
                }
            } else if (!item.isNoSelectionOption()) {
                String convertedValue = converter.getAsString(context, component, item.getValue());
                if (value.equals(convertedValue)) {
                    return item.getValue();
                }
            }
        }
        return null;
    }

    private static List<SelectItem> collectSelectItems(UIComponent component) {
        List<SelectItem> items = new ArrayList<>();
        for (UIComponent child : component.getChildren()) {
            if (child instanceof UISelectItem) {
                addSelectItem(items, (UISelectItem) child);
            } else if (child instanceof UISelectItems) {
                addSelectItems(items, ((UISelectItems) child).getValue());
            }
        }
        return items;
    }

    private static void addSelectItem(List<SelectItem> items, UISelectItem uiSelectItem) {
        Object value = uiSelectItem.getValue();
        if (value instanceof SelectItem) {
            items.add((SelectItem) value);
            return;
        }

        items.add(new SelectItem(
                uiSelectItem.getItemValue(),
                uiSelectItem.getItemLabel(),
                uiSelectItem.getItemDescription(),
                uiSelectItem.isItemDisabled(),
                uiSelectItem.isItemEscaped(),
                uiSelectItem.isNoSelectionOption()
        ));
    }

    @SuppressWarnings("unchecked")
    private static void addSelectItems(List<SelectItem> items, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof SelectItem) {
            items.add((SelectItem) value);
            return;
        }
        if (value instanceof SelectItem[]) {
            for (SelectItem item : (SelectItem[]) value) {
                items.add(item);
            }
            return;
        }
        if (value instanceof Iterable) {
            for (Object item : (Iterable<Object>) value) {
                addSelectItems(items, item);
            }
            return;
        }
        if (value instanceof Map) {
            for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) value).entrySet()) {
                items.add(new SelectItem(entry.getValue(), String.valueOf(entry.getKey())));
            }
            return;
        }
        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                addSelectItems(items, Array.get(value, i));
            }
            return;
        }
        items.add(new SelectItem(value, String.valueOf(value)));
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    static class ArrayIterator implements Iterator<SelectItem> {
        private final SelectItem[] items;
        private int index = 0;

        ArrayIterator(SelectItem[] items) {
            this.items = items;
        }

        @Override
        public boolean hasNext() {
            return index < items.length;
        }

        @Override
        public SelectItem next() {
            return items[index++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
