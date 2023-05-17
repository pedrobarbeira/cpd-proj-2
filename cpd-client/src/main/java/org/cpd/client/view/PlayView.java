package org.cpd.client.view;

import org.cpd.client.controller.GameController;
import org.cpd.shared.User;

public class PlayView {
    private final GameController controller;
    private final User user;
    public PlayView(User user, GameController controller) {
        this.controller = controller;
        this.user = user;
    }

    public Object run() {
        return null;
    }
}
