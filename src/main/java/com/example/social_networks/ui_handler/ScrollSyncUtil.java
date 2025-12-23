package com.example.social_networks.ui_handler;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class ScrollSyncUtil {

    public static void bind(TextArea area, ScrollPane numbers) {
        area.lookupAll(".scroll-bar").forEach(node -> {
            if (node instanceof ScrollBar sb &&
                    sb.getOrientation() == Orientation.VERTICAL) {

                numbers.vvalueProperty()
                        .bindBidirectional(sb.valueProperty());
            }
        });
    }
}
