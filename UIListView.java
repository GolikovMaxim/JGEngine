package JGEngine;

import javafx.scene.control.ListView;

public class UIListView extends Component {
    ListView listView;

    public UIListView(ListView listView) {
        this.listView = listView;
    }

    @Override
    public void onStart() {
        if(gameObject.getComponent(Renderer2D.class) == null) {
            gameObject.addComponent(new Renderer2D());
        }
        gameObject.getComponent(Renderer2D.class).setListView(listView);
    }

    public void addToList(Object obj) {
        RenderSystem.renderSystem.renderings.add(() -> listView.getItems().add(obj));
    }

    public void clearList() {
        RenderSystem.renderSystem.renderings.add(() -> listView.getItems().clear());
    }

    public void setListFocusable(boolean focus) {
        listView.setFocusTraversable(focus);
    }

    public Object getSelectedItem() {
        return listView.getSelectionModel().getSelectedItem();
    }
}
